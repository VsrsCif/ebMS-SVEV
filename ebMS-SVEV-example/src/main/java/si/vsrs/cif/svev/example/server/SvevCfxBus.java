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

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.cxf.ws.security.wss4j.CryptoCoverageChecker;
import org.apache.cxf.ws.security.wss4j.CryptoCoverageUtil;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;

/**
 *
 * @author Jože Rihtaršič
 */
public class SvevCfxBus {
    WSS4JInInterceptor mwssInterceptor = null;
    CryptoCoverageChecker mccCoverageChecker = null;
    WSS4JOutInterceptor mwssOutInterceptor = null;

    public SvevCfxBus() {
    }

    public WSS4JInInterceptor getServerWSInterceptor() {
        if (mwssInterceptor == null) {
            Map<String, Object> inProps = new HashMap<>();
            inProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
            inProps.put(WSHandlerConstants.SIG_PROP_FILE, "security/msh_e-box-b_signVer.properties");
            mwssInterceptor = new WSS4JInInterceptor(inProps);
        }
        return mwssInterceptor;
    }

    public CryptoCoverageChecker getServerCryptoCoverageChecker() {
        if (mccCoverageChecker == null) {
            Map<String, String> prefixes = new HashMap<>();
            prefixes.put("eb3", "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/");
            prefixes.put("env", "http://www.w3.org/2003/05/soap-envelope");
            List<CryptoCoverageChecker.XPathExpression> xpaths = Arrays.asList(new CryptoCoverageChecker.XPathExpression("env:Header/eb3:Messaging", CryptoCoverageUtil.CoverageType.SIGNED, CryptoCoverageUtil.CoverageScope.ELEMENT), new CryptoCoverageChecker.XPathExpression("env:Body", CryptoCoverageUtil.CoverageType.SIGNED, CryptoCoverageUtil.CoverageScope.ELEMENT));
            mccCoverageChecker = new CryptoCoverageChecker(prefixes, xpaths);
        }
        return mccCoverageChecker;
    }

    public WSS4JOutInterceptor getServerWSOutInterceptor() {
        if (mwssOutInterceptor == null) {
            Map<String, Object> outProps = new HashMap<>();
            StringWriter elmWr = new StringWriter();
            elmWr.write("{Element}{http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/}Messaging;");
            elmWr.write("{Element}{http://www.w3.org/2003/05/soap-envelope}Body;");
            outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
            outProps.put(WSHandlerConstants.SIGNATURE_USER, "msh.e-box-b.si");
            outProps.put(WSHandlerConstants.USER, "msh.e-box-b.si");
            outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, KeyPasswordCallback.class.getName());
            outProps.put(WSHandlerConstants.SIG_PROP_FILE, "security/msh_e-box-b_sign.properties");
            outProps.put(WSHandlerConstants.SIGNATURE_PARTS, elmWr.toString());
            outProps.put(WSHandlerConstants.SIG_ALGO, "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512");
            outProps.put(WSHandlerConstants.SIG_DIGEST_ALGO, "http://www.w3.org/2001/04/xmlenc#sha256");
            mwssOutInterceptor = new WSS4JOutInterceptor(outProps);
        }
        return mwssOutInterceptor;
    }
    
}
