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



/**
 *
 * @author Jože Rihtaršič
 */
public class EBMSError extends Exception{
    
    EBMSErrorCode ebmsErrorCode;
    String subMessage;
    String refToMessage;
    
    public EBMSError(EBMSErrorCode ec, String refToMsg) {
        ebmsErrorCode = ec;
        refToMessage =  refToMsg;
    }

    public EBMSError(EBMSErrorCode ec, String refToMsg,  String message) {
        super(ec.getName());
        ebmsErrorCode = ec;
        refToMessage =  refToMsg;
        subMessage =  message;
        
    }

    public EBMSError(EBMSErrorCode ec,String refToMsg, String message, Throwable cause) {
        super(ec.getName(), cause);
        ebmsErrorCode = ec;
        refToMessage =  refToMsg;
        subMessage =  message;
    }

    public EBMSError(EBMSErrorCode ec,String refToMsg, Throwable cause) {
        super(ec.getName(), cause);
        ebmsErrorCode = ec;
        refToMessage =  refToMsg;
        
    }

    public String getSubMessage() {
        return subMessage;
    }

    public EBMSErrorCode getEbmsErrorCode() {
        return ebmsErrorCode;
    }

    public String getRefToMessage() {
        return refToMessage;
    }
    

    
    
}
