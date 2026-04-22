<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="selectedSkill"/>

    <xsl:output method="html" indent="yes" encoding="UTF-8"/>

    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Recipes Displayed with XSL</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 30px; background-color: #f8f9fa; color: #222; }
                    h1 { color: #2c3e50; }
                    table { width: 100%; border-collapse: collapse; background: white; margin-top: 20px; }
                    th, td { border: 1px solid #ccc; padding: 10px; text-align: left; }
                    th { background-color: #2c3e50; color: white; }
                    .match { background-color: yellow; }
                    .no-match { background-color: lightgreen; }
                </style>
            </head>
            <body>
                <h1>Recipes Displayed Using XSL</h1>
                <p>Selected user cooking skill: <strong><xsl:value-of select="$selectedSkill"/></strong></p>

                <table>
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Cuisine Types</th>
                        <th>Difficulty</th>
                    </tr>

                    <xsl:for-each select="recipes/recipe">
                        <tr>
                            <xsl:attribute name="class">
                                <xsl:choose>
                                    <xsl:when test="difficulty = $selectedSkill">match</xsl:when>
                                    <xsl:otherwise>no-match</xsl:otherwise>
                                </xsl:choose>
                            </xsl:attribute>

                            <td><xsl:value-of select="@id"/></td>
                            <td><xsl:value-of select="title"/></td>
                            <td>
                                <xsl:for-each select="cuisineTypes/cuisine">
                                    <xsl:value-of select="."/>
                                    <xsl:if test="position() != last()">, </xsl:if>
                                </xsl:for-each>
                            </td>
                            <td><xsl:value-of select="difficulty"/></td>
                        </tr>
                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>