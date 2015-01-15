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

package si.vsrs.cif.svev.example.exception;

import javax.xml.namespace.QName;

/**
 *
 * @author Jože Rihtaršič
 */
public enum SOAPExceptionCode {
    StoreInboundMailFailure("StoreInboundMailFailure", new QName("http://jmsh.org/svev-msh", "0001")),
    SoapVersionMismatch("SoapVersionMismatch", new QName("http://jmsh.org/svev-msh", "0002")),
    SoapParseFailure("SoapParseFailure", new QName("http://jmsh.org/svev-msh", "0003"));
    
    String name;
    QName code;

    private SOAPExceptionCode(String name, QName code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public QName getCode() {
        return code;
    }

    public void setCode(QName code) {
        this.code = code;
    }
            
    
    
}
