<?xml version="1.0" encoding="utf-8"?>
<!-- edited with XMLSpy v2008 (http://www.altova.com) by XMLSpy 2007 Professional Ed., Installed for 5 users (with SMP from 2007-02-06 to 2008-02-07) (CIF VSRS) -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0"  
                    xmlns:S12="http://www.w3.org/2003/05/soap-envelope" 
                    xmlns:ns3="http://jmsh.org/svevencryptionkey"
                    exclude-result-prefixes="S12 eb3" xmlns:wsa="http://www.w3.org/2005/08/addressing"  xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" xmlns:eb3="http://docs.oasis-open.org/ebxml-msg/ebms/v3.0/ns/core/200704/" xmlns:ds="http://www.w3.org/2000/09/xmldsig#" xmlns:ebbp="http://docs.oasis-open.org/ebxml-bp/ebbp-signals-2.0"    >
	<xsl:output method="xml" indent="yes"/>

	<xsl:template match="S12:Envelope">
		<xsl:apply-templates/>
	</xsl:template>
	<xsl:template match="S12:Header">
		<xsl:copy-of select="eb3:Messaging/eb3:SignalMessage/ns3:SVEVEncryptionKey"/>
	</xsl:template>
	<xsl:template match="S12:Envelope/S12:Body">
    </xsl:template>
    
</xsl:stylesheet>
