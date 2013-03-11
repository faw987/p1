<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ns13="http://www.telcordia.com/dcat/ws/v2.0/dto/ResourceOffering"  xmlns:ns5="http://www.telcordia.com/dcat/ws/v2.0/dto/common/offering">

<xsl:output method="html" indent="yes" version="4.0"/>
<!--   <xsl:template match="ns13:ResourceOffering"><html><table border="1"> ALSO remove ns5: below-->
<xsl:template match="ProductOffering"><html><table border="1">
<tr>

      <td>"ns5:offerCharInstId"</td>

</tr>

<xsl:for-each select ="OfferingCharacteristic">
<tr>

      <td><xsl:value-of select="offerCharInstId"/></td>
      <td bgcolor="#FCDFFF">
      <a HREF="http://www.telcordia.com">
      <xsl:value-of select="relName"/></a></td>
      <td bgcolor="#E0FFFF"><xsl:value-of select="groupName"/></td>
      <td><xsl:value-of select="value"/></td>
</tr>

</xsl:for-each></table></html>

</xsl:template>


</xsl:stylesheet>