//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.11 at 04:30:18 PM CET 
//


package org.w3._2003._05.soap_envelope;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.w3._2003._05.soap_envelope package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.w3._2003._05.soap_envelope
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Envelope }
     * 
     */
    public Envelope createEnvelope() {
        return new Envelope();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link Body }
     * 
     */
    public Body createBody() {
        return new Body();
    }

    /**
     * Create an instance of {@link Fault }
     * 
     */
    public Fault createFault() {
        return new Fault();
    }

    /**
     * Create an instance of {@link Faultcode }
     * 
     */
    public Faultcode createFaultcode() {
        return new Faultcode();
    }

    /**
     * Create an instance of {@link Faultreason }
     * 
     */
    public Faultreason createFaultreason() {
        return new Faultreason();
    }

    /**
     * Create an instance of {@link Detail }
     * 
     */
    public Detail createDetail() {
        return new Detail();
    }

    /**
     * Create an instance of {@link NotUnderstood }
     * 
     */
    public NotUnderstood createNotUnderstood() {
        return new NotUnderstood();
    }

    /**
     * Create an instance of {@link Upgrade }
     * 
     */
    public Upgrade createUpgrade() {
        return new Upgrade();
    }

    /**
     * Create an instance of {@link SupportedEnvType }
     * 
     */
    public SupportedEnvType createSupportedEnvType() {
        return new SupportedEnvType();
    }

    /**
     * Create an instance of {@link Reasontext }
     * 
     */
    public Reasontext createReasontext() {
        return new Reasontext();
    }

    /**
     * Create an instance of {@link Subcode }
     * 
     */
    public Subcode createSubcode() {
        return new Subcode();
    }

}
