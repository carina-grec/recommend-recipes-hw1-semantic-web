package com.example.util;

import java.io.File;

public class XmlFileUtil {

    private XmlFileUtil() {
    }

    public static File getRecipesXmlFile() {
        return new File("src/main/resources/data/recipes.xml");
    }

    public static File getUsersXmlFile() {
        return new File("src/main/resources/data/users.xml");
    }

    public static File getRecipesXsdFile() {
        return new File("src/main/resources/data/recipes.xsd");
    }

    public static File getUsersXsdFile() {
        return new File("src/main/resources/data/users.xsd");
    }

    public static File getRecipesXslFile() {
        return new File("src/main/resources/xsl/recipes.xsl");
    }
}