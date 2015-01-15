//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.11 at 04:30:18 PM CET 
//


package org.jmsh.svevencryptionkey;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="KeyValue" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="keyAlgorithm" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="keyFormat" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="ivByteParameter" type="{http://www.w3.org/2001/XMLSchema}base64Binary" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "keyValue"
})
@XmlRootElement(name = "SVEVEncryptionKey")
public class SVEVEncryptionKey
    implements Serializable
{

    @XmlElement(name = "KeyValue", required = true)
    protected byte[] keyValue;
    @XmlAttribute(name = "id", required = true)
    protected String id;
    @XmlAttribute(name = "keyAlgorithm")
    protected String keyAlgorithm;
    @XmlAttribute(name = "keyFormat")
    protected String keyFormat;
    @XmlAttribute(name = "ivByteParameter")
    protected byte[] ivByteParameter;

    /**
     * Gets the value of the keyValue property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getKeyValue() {
        return keyValue;
    }

    /**
     * Sets the value of the keyValue property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setKeyValue(byte[] value) {
        this.keyValue = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the keyAlgorithm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyAlgorithm() {
        return keyAlgorithm;
    }

    /**
     * Sets the value of the keyAlgorithm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyAlgorithm(String value) {
        this.keyAlgorithm = value;
    }

    /**
     * Gets the value of the keyFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyFormat() {
        return keyFormat;
    }

    /**
     * Sets the value of the keyFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyFormat(String value) {
        this.keyFormat = value;
    }

    /**
     * Gets the value of the ivByteParameter property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getIvByteParameter() {
        return ivByteParameter;
    }

    /**
     * Sets the value of the ivByteParameter property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setIvByteParameter(byte[] value) {
        this.ivByteParameter = value;
    }

}