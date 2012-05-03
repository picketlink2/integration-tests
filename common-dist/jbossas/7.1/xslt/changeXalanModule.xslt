<?xml version="1.0" encoding="UTF-8"?>
<!-- XSLT file to add the security domains to the standalone.xml used during 
	the integration tests. -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:module="urn:jboss:module:1.1" version="1.0">

	<xsl:output method="xml" indent="yes" />

	<xsl:template match="//module:module[@name='org.apache.xalan']" />

	<xsl:template match="/">
		<module xmlns="urn:jboss:module:1.1" name="org.apache.xalan">
			<resources>
				<resource-root path="serializer-2.7.1.jbossorg-1.jar" />
				
				<!-- Workaround to make the IDP work in AS7 -->
				<!--resource-root path="xalan-2.7.1.jbossorg-1.jar"/ -->
				<resource-root path="xalan-2.7.1.jar" />
				<!-- Insert resources here -->
			</resources>
			<dependencies>
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