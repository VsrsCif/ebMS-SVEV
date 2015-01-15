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

import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.phase.Phase;
import si.vsrs.cif.svev.example.utils.EbMSConstants;

/**
 *
 * @author Jože Rihtaršič
 */
public class EBMMustUderstandInterceptor extends AbstractSoapInterceptor {

    private static final Set<QName> HEADERS = new HashSet<>();

    static {
        HEADERS.add(new QName(EbMSConstants.EbMs_NS, EbMSConstants.EbMs_RootElementName));
    }

    public EBMMustUderstandInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    public EBMMustUderstandInterceptor(String phase) {
        super(phase);
    }

    @Override
    public Set<QName> getUnderstoodHeaders() {
        return HEADERS;
    }

    @Override
    public void handleMessage(SoapMessage msg) {
    }
}
