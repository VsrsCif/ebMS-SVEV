
package si.vsrs.cif.svev.example.test.server;

import javax.xml.ws.Endpoint;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;


public class MSHTestServer {
    
    MSHTestProvider mmshImpl = new MSHTestProvider();

    
    EndpointImpl mmshEndpoint = null;

    
    public MSHTestServer() {
    }
    
    public MSHTestServer(String mshUrl) {
        restart(mshUrl);
        
    }
    
    public void stop() {
        if (mmshEndpoint != null) {
            mmshEndpoint.stop();
            mmshEndpoint = null;
        }
      
        
    }
    
    public final void restart(String mshUrl) {
        
        stop();
        mmshEndpoint = (EndpointImpl) Endpoint.publish(mshUrl, mmshImpl);
        mmshEndpoint.getInInterceptors().add(new LoggingInInterceptor());
       mmshEndpoint.getInInterceptors().add(new MustUderstandInterceptor()); // add only to "uderstand EMBS headers"!
        mmshEndpoint.getOutInterceptors().add(new LoggingOutInterceptor());

    }
     
    
    
}
