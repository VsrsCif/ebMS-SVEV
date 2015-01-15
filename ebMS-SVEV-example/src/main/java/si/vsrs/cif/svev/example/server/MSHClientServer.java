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

import javax.xml.ws.Endpoint;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
/**
 *
 * @author Jože Rihtaršič
 */
public class MSHClientServer extends SvevCfxBus {

    MSHClientProvider mmshImpl = new MSHClientProvider();

    EndpointImpl mmshEndpoint = null;

    public MSHClientServer() {
    }

    public MSHClientServer(String mshUrl) {
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
        mmshEndpoint.getInInterceptors().add(new EBMMustUderstandInterceptor()); // add only to "uderstand EMBS headers"!
        mmshEndpoint.getInInterceptors().add(getServerWSInterceptor()); // checks if signature is ok
        mmshEndpoint.getInInterceptors().add(getServerCryptoCoverageChecker()); // checks if elements are signed

        mmshEndpoint.getOutInterceptors().add(new LoggingOutInterceptor());
        mmshEndpoint.getOutInterceptors().add(getServerWSOutInterceptor());

    }


}
