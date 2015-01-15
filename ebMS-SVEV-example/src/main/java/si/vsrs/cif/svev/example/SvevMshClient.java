/*
* Copyright 2015, Supreme Court Republic of Slovenia 
*
* Licensed under the EUPL, Version 1.1 or – as soon they will be approved by 
* the European Commission - subsequent versions of the EUPL (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* https://joinup.ec.europa.eu/software/page/eupl
*
* Unless required by applicable law or agreed to in writing, software 
* distributed under the Licence is distributed on an "AS IS" basis, WITHOUT 
* WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and  
* limitations under the Licence.
*/

package si.vsrs.cif.svev.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.soap.SOAPBinding;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;

import org.jmsh.mshpayload.MSHPart;
import org.jmsh.mshpayload.MSHPayload;
import org.jmsh.svevencryptionkey.SVEVEncryptionKey;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;
import si.vsrs.cif.svev.example.exception.FOPException;
import si.vsrs.cif.svev.example.server.EBMMustUderstandInterceptor;
import si.vsrs.cif.svev.example.server.SvevCfxBus;
import si.vsrs.cif.svev.example.utils.DCLogger;
import si.vsrs.cif.svev.example.utils.EBMSUtils;
import si.vsrs.cif.svev.example.utils.FOPUtils;
import si.vsrs.cif.svev.example.utils.Settings;
import si.vsrs.cif.svev.example.utils.SvevCrypto;
import si.vsrs.cif.svev.example.utils.XMLUtils;
import org.w3._2001._04.xmlenc_.EncryptedData;
import org.w3c.dom.Element;
import si.vsrs.cif.svev.example.utils.PDFSignature;


/**
 *
 * @author Jože Rihtaršič
 */
public class SvevMshClient extends SvevCfxBus {

    protected final DCLogger mlog = new DCLogger(SvevMshClient.class);
    // encryption priperties
    SvevCrypto.SymEncAlgorithms mAlgorithem = SvevCrypto.SymEncAlgorithms.AES128_CBC;
    SvevCrypto mscCrypto = new SvevCrypto();
    EBMSUtils mebmsUtils = new EBMSUtils();

    Settings mSettings = Settings.getInstance();
    FOPUtils mfop = new FOPUtils(mSettings.getFopConfigFile());



    public void getKeyAndDecryptPayload(File inRequest, String strUrl) {

        try {
            // generate message
            File f = mfop.generateVisualizationToFile(inRequest, SvevMshClient.class.getResourceAsStream("/xslt/adviceOfDelivery.fo"));
            File fs = signFile(f);
            byte[] buf = readFile(fs);

            MSHPayload mshPayload = new MSHPayload();
              String plId = UUID.randomUUID().toString();
            MSHPart mp1 = new MSHPart();
            mp1.setData(new MSHPart.Data());
            mp1.setId(plId);
            mp1.setName("Vročilnica");
            mp1.setMimeType("application/pdf");
            mp1.setEncoding("base64");
            mp1.getData().getContent().add(Base64.getEncoder().encodeToString(buf));
            mshPayload.getMSHParts().add(mp1);

            Messaging msg =  generateMessage(inRequest, plId);
            
            // create soap message
            MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            SOAPMessage request = mf.createMessage();            
            Marshaller marshaller = JAXBContext.newInstance(MSHPayload.class, Messaging.class).createMarshaller();
            
            // add soap body
            marshaller.marshal(mshPayload, request.getSOAPBody());
            // add soap header
            marshaller.marshal(msg, request.getSOAPHeader());
             
            request.saveChanges();
            
            // push AdviceOfDelivery
            Dispatch<SOAPMessage> client = getClient(strUrl);
            SOAPMessage respose =  client.invoke(request);
            // get key 
            
            DOMSource dr = new DOMSource(respose.getSOAPPart());
            SVEVEncryptionKey key =  (SVEVEncryptionKey)XMLUtils.deserialize(dr, SvevMshClient.class.getResourceAsStream("/xslt/getKeyFromSoap.xsl"), SVEVEncryptionKey.class);
            // deserialize payload!
            
            
            
            MSHPayload pl =  (MSHPayload)XMLUtils.deserialize(new FileInputStream(inRequest),
                    SvevMshClient.class.getResourceAsStream("/xslt/getPayloadFromSoap.xsl"), MSHPayload.class);

            for (MSHPart mp: pl.getMSHParts()){
                for (Object o : mp.getData().getContent()){
                    if (o instanceof Element) {
                        Element e = (Element)o;                       
                        EncryptedData dt = (EncryptedData)XMLUtils.deserialize(e, EncryptedData.class);
                        byte[] buff =  mscCrypto.dencryptData((EncryptedData)dt, key);
                      
                       System.out.println("DECRYPTED DATA: " + new String(buff, "UTF-8") );
                    }
                }
            }
            
            

                
            // decrypt payloads.. 
        } catch (FOPException | IOException | SOAPException | JAXBException | TransformerException ex) {
            Logger.getLogger(SvevMshClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private byte[] readFile(File fn) throws IOException {
        byte[] bf = null;
        try (FileInputStream fis = new FileInputStream(fn)) {
            bf = new byte[fis.available()];
            fis.read(bf);
        }
        return bf;
    }

    private File signFile(File document) {
        
         PDFSignature sigUtil = new PDFSignature();
         // first signatire by receiver
         File f = sigUtil.signPDF(document, SvevMshClient.class.getResourceAsStream("/security/keystore/btestko.p12"), "key1234","key1234", "PKCS12", "btestko uporabnik", true);
         // secod  signature by receiver SVEV
        return  sigUtil.signPDF(f, SvevMshClient.class.getResourceAsStream("/security/keystore/msh.e-box-b-keystore.jks"), "test1234", "key1234","JKS", "msh.e-box-b.si",false);

    }

    private Messaging generateMessage(File inRequest, String payloadId) {
        Messaging msg = mebmsUtils.createMessaging();
        Messaging msgIn = null;
        try (FileInputStream fis = new FileInputStream(inRequest)) {

            msgIn = (Messaging) XMLUtils.deserialize(fis, SvevMshClient.class.getResourceAsStream("/xslt/getMessagingFromSoap.xsl"), Messaging.class);
        } catch (IOException | JAXBException | TransformerException ex) {
            Logger.getLogger(SvevMshClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        UserMessage um = mebmsUtils.createUserMessage(msgIn.getUserMessages().get(0), mSettings.getDomain(), payloadId);
        msg.getUserMessages().add(um);
        return msg;
    }

    private Dispatch<SOAPMessage> getClient(String url) {
        QName serviceName1 = new QName("http://server.msh.jrc.si/", "MSHProviderService");
        QName portName1 = new QName("http://server.msh.jrc.si/", "MSHProviderPort");
        Service s = Service.create(serviceName1);
        s.addPort(portName1, SOAPBinding.SOAP12HTTP_BINDING, url);

        Dispatch<SOAPMessage> dispSOAPMsg = s.createDispatch(portName1, SOAPMessage.class, Service.Mode.MESSAGE);
        // configure interceptors
        Client cxfClient = ((org.apache.cxf.jaxws.DispatchImpl) dispSOAPMsg).getClient();

        cxfClient.getInInterceptors().add(new LoggingInInterceptor());
        cxfClient.getInInterceptors().add(new EBMMustUderstandInterceptor()); // add only to "uderstand EMBS headers"!
        cxfClient.getInInterceptors().add(getServerWSInterceptor()); // checks if signature is ok
        cxfClient.getInInterceptors().add(getServerCryptoCoverageChecker()); // checks if elements are signed

        cxfClient.getOutInterceptors().add(new LoggingOutInterceptor());
        cxfClient.getOutInterceptors().add(getServerWSOutInterceptor());

        return dispSOAPMsg;
    }

   
}
