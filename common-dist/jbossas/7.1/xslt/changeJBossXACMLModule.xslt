<?xml version="1.0" encoding="UTF-8"?>
<!-- XSLT file to add the security domains to the standalone.xml used during 
	the integration tests. -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:module="urn:jboss:module:1.1" version="1.0">

	<xsl:output method="xml" indent="yes" />

	<xsl:template match="//module:module[@name='org.jboss.security.xacml']" />

	<xsl:template match="/">
		<module xmlns="urn:jboss:module:1.1" name="org.jboss.security.xacml">
			<properties>
				<property name="jboss.api" value="private" />
			</properties>

			<resources>
				<resource-root path="jbossxacml-2.0.8.Final.jar" />
			</resources>

			<dependencies>
				<module name="org.apache.santuario.xmlsec" />
				<module name="javax.xml.bind.api" />
				<module name="javax.api" />
			</dependencies>
		</module>
	</xsl:template>

	<!-- Copy everything else. -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>