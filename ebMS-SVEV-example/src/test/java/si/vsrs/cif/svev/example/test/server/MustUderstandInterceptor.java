/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package si.vsrs.cif.svev.example.test.server;

import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import si.vsrs.cif.svev.example.utils.EbMSConstants;


/**
 *
 * @author sluzba
 */
public class MustUderstandInterceptor extends AbstractSoapInterceptor {

    private static final Set<QName> HEADERS = new HashSet<>();

    static {
        HEADERS.add(new QName(EbMSConstants.EbMs_NS, EbMSConstants.EbMs_RootElementName));
         WSS4JInInterceptor me = new WSS4JInInterceptor();
        HEADERS.addAll(me.getUnderstoodHeaders());
    }

    public MustUderstandInterceptor() {
        super(Phase.PRE_PROTOCOL);
    }

    public MustUderstandInterceptor(String phase) {
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
