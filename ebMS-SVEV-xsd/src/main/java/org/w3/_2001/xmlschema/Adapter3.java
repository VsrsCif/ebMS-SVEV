//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.11 at 04:30:18 PM CET 
//


package org.w3._2001.xmlschema;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter3
    extends XmlAdapter<String, Integer>
{


    public Integer unmarshal(String value) {
        return ((int)javax.xml.bind.DatatypeConverter.parseInt(value));
    }

    public String marshal(Integer value) {
        if (value == null) {
            return null;
        }
        return (javax.xml.bind.DatatypeConverter.printInt((int)(int)value));
    }

}
