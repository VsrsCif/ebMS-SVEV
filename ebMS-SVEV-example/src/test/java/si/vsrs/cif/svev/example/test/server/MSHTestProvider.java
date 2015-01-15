
package si.vsrs.cif.svev.example.test.server;

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
import javax.xml.ws.soap.SOAPBinding;
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
import si.vsrs.cif.svev.example.server.MSHClientProvider;
import si.vsrs.cif.svev.example.server.MSHServerTest;
import si.vsrs.cif.svev.example.utils.Settings;
import si.vsrs.cif.svev.example.utils.XMLUtils;


// dummy server implementation if it gets request then eponse key:

@WebServiceProvider()
@ServiceMode(value = Service.Mode.MESSAGE)
@BindingType(SOAPBinding.SOAP12HTTP_BINDING)

public class MSHTestProvider implements Provider<SOAPMessage> {

    Settings mSettings = Settings.getInstance();
    

    @Resource
    WebServiceContext wsContext;

    public MSHTestProvider() {

    }

    @Override
    public SOAPMessage invoke(SOAPMessage request) {
        SOAPMessage response = null;
        try {
            
            // store request
            File f = File.createTempFile("gen-AdviceOfReceipt", ".xml", new  File("unit-test-data"));
            try (FileOutputStream fos = new FileOutputStream(f)){
                request.writeTo(fos);
            }
            
            // response key!
             MessageFactory mf = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
            response = mf.createMessage();    
            
            DOMResult drRes  = new DOMResult(response.getSOAPPart());
            
            // generate AS4 Set message id and timestamp with xalan transformation or progamatically!!
            XMLUtils.deserialize(MSHServerTest.class.getResourceAsStream("/test-samples/TEST_01-AdviceOfDelivery-Response.xml"), drRes);            
            response.saveChanges();
            
            
        } catch (IOException ex) {
            Logger.getLogger(MSHTestProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SOAPException ex) {
                Logger.getLogger(MSHTestProvider.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JAXBException ex) {
            Logger.getLogger(MSHTestProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(MSHTestProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
}
