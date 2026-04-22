package com.example.util;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;

public class XmlValidationUtil {

    private XmlValidationUtil() {
    }

    public static boolean validateXml(File xmlFile, File xsdFile) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(xsdFile);
            javax.xml.validation.Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xmlFile));
            return true;
        } catch (SAXException | IOException e) {
            System.out.println("Validation error: " + e.getMessage());
            return false;
        }
    }
}