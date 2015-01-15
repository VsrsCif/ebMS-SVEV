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

package si.vsrs.cif.svev.example.utils;

/**
 *
 * @author Jože Rihtaršič
 */
public class EbMSConstants {
    // schema constats
    public static final String EbMs_NS = "http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/";
    public static final String EbMs_RootElementName = "Messaging";
    
    // party info
    public static final String EbMs_PARTY_TYPE_EBOX  = "urn:oasis:names:tc:ebcore:partyid-type:unregistered:si-svev:e-box";
    public static final String EbMs_PARTY_TYPE_NAME  = "urn:oasis:names:tc:ebcore:partyid-type:unregistered:si-svev:name";
    public static final String EbMs_PROPERTY_DESC  = "description";
            
    // cfx context parameters
    public static final String ContextProperty_ADDRESSE = "si.jrc.msh.addresse";
    public static final String ContextProperty_ConversationId = "si.jrc.msh.conversationId";
    public static final String ContextProperty_Service = "si.jrc.msh.service";
    public static final String ContextProperty_Action = "si.jrc.msh.action";
    
    public static final String ContextProperty_Out_SOAP_Message_File = "si.jrc.outgoing.soap.message.file";
    public static final String ContextProperty_In_SOAP_Message_File = "si.jrc.incoming.soap.message.file";
    
    
    // log soap file prefix/suffix
    public static final String File_Suffix_SOAP_Message_Request = "-Request.xml";
    public static final String File_Suffix_SOAP_Message_Response = "-Response.xml";    
    public static final String File_PREFIX_SOAP_Message = "EBMS-";
    
    // legal delivery actions
    public static final String LegalDeliveryAction_DeliveryNotification = "DeliveryNotification";
    public static final String LegalDeliveryAction_AdviceOfDelivery = "AdviceOfDelivery";
    public static final String LegalDeliveryAction_FictionNotification = "FictionNotification";

    
}
