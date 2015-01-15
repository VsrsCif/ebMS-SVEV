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
package si.vsrs.cif.svev.example.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.ws.BindingType;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.soap.SOAPBinding;
import si.vsrs.cif.svev.example.utils.Settings;
import si.vsrs.cif.svev.example.utils.XMLUtils;

/**
 *
 * @author Jože Rihtaršič
 */
@WebServiceProvider()
@ServiceMode(value = Service.Mode.MESSAGE)
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)
public class MSHClientProvider implements Provider<SOAPMessage> {

    Settings mSettings = Settings.getInstance();
    

    @Resource
    WebServiceContext wsContext;

    public MSHClientProvider() {

    }

    @Override
    public SOAPMessage invoke(SOAPMessage request) {
        SOAPMessage response = null;
        try {
            
            //validate request
            //bussines logic.. 
            
            // generate AS4Receipt with as4 xslt:
            // http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/profiles/AS4-profile/v1.0/os/examples/as4receipt/
            MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            response = mf.createMessage();
            
            DOMSource srReq = new DOMSource(request.getSOAPPart());
            DOMResult drRes  = new DOMResult(response.getSOAPPart());
            
            // generate AS4 Set message id and timestamp with xalan transformation or progamatically!!
            XMLUtils.deserialize(srReq, drRes, MSHClientProvider.class.getResourceAsStream("/xslt/as4receipt.xsl"));            
            
            response.saveChanges();
            request.writeTo(new FileOutputStream(File.createTempFile("svev-", ".xml", Settings.getInstance().getInboxFolder())));
            
        } catch (SOAPException | JAXBException | TransformerException  | IOException ex) {
            Logger.getLogger(MSHClientProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // generate delivery advice and push it to court
        return response;
    }
}
