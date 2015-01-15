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

import java.util.Date;
import java.util.UUID;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Messaging;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;

/**
 *
 * @author Jože Rihtaršič
 */
public class EBMSUtils {

    private static final String ID_PREFIX_ = "SVEV-";

    public Messaging createMessaging() {
        Messaging msg = new Messaging();
        //ID must be an NCName. This means that it must start with a letter or underscore, 
        //and can only contain letters, digits, underscores, hyphens, and periods.
        msg.setId(ID_PREFIX_ + UUID.randomUUID().toString()); // generate unique id

        msg.setMustUnderstand(Boolean.TRUE);

        return msg;

    }

    private MessageInfo createMessageInfo(String senderDomain, String refToMessage) {
        return createMessageInfo(UUID.randomUUID().toString(), senderDomain, refToMessage);
    }

    private MessageInfo createMessageInfo(String msgId, String senderDomain, String refToMessage) {
        MessageInfo mi = new MessageInfo();
        mi.setMessageId(msgId + "@" + senderDomain);
        mi.setTimestamp(new Date());
        mi.setRefToMessageId(refToMessage);
        return mi;
    }

    public UserMessage createUserMessage(UserMessage inputMessage, String senderDomain, String payloadId) {

        UserMessage usgMsg = new UserMessage();

        // --------------------------------------
        // generate  MessageInfo 
        MessageInfo mi = createMessageInfo(null, senderDomain, inputMessage.getMessageInfo().getMessageId());
        usgMsg.setMessageInfo(mi);

        // generate from 
        usgMsg.setPartyInfo(new PartyInfo());
        usgMsg.getPartyInfo().setFrom(new From());
        // PUSH - MEP
        usgMsg.getPartyInfo().getFrom().setRole("si-svev:sender");
        // add sender id
        usgMsg.getPartyInfo().getFrom().getPartyIds().addAll(inputMessage.getPartyInfo().getTo().getPartyIds());

        // generate to
        usgMsg.getPartyInfo().setTo(new To());
        usgMsg.getPartyInfo().getTo().setRole("si-svev:receiver");
        usgMsg.getPartyInfo().getTo().getPartyIds().addAll(inputMessage.getPartyInfo().getFrom().getPartyIds());

        // set colloboration info
        //BusinessInfo bi = pm.getLegs().get(0).getBusinessInfo();
        usgMsg.setCollaborationInfo(new CollaborationInfo());
        usgMsg.getCollaborationInfo().setService(new org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service());
        usgMsg.getCollaborationInfo().getService().setValue(inputMessage.getCollaborationInfo().getService().getValue());
        usgMsg.getCollaborationInfo().setAction(EbMSConstants.LegalDeliveryAction_AdviceOfDelivery);
        usgMsg.getCollaborationInfo().setConversationId(inputMessage.getCollaborationInfo().getConversationId());
        usgMsg.getCollaborationInfo().setAgreementRef(inputMessage.getCollaborationInfo().getAgreementRef());

        // add mail description
        MessageProperties mp = new MessageProperties();
        Property p = new Property();
        p.setName(EbMSConstants.EbMs_PROPERTY_DESC);
        p.setValue("Vročilnica");
        mp.getProperties().add(p);
        usgMsg.setMessageProperties(mp);

        // add pyload info
        usgMsg.setPayloadInfo(new PayloadInfo());

        PartInfo pl = new PartInfo();
        pl.setHref("#" + payloadId);
        usgMsg.getPayloadInfo().getPartInfos().add(pl);
        usgMsg.getPayloadInfo().getPartInfos().add(pl);

        return usgMsg;
    }

}
