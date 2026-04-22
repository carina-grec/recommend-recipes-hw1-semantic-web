package com.example.service;

import com.example.util.XmlFileUtil;
import org.springframework.stereotype.Service;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.StringWriter;

@Service
public class XslTransformationService {

    public String transformRecipesXml(String selectedSkill) {
        try {
            File xmlFile = XmlFileUtil.getRecipesXmlFile();
            File xslFile = XmlFileUtil.getRecipesXslFile();

            Source xmlSource = new StreamSource(xmlFile);
            Source xslSource = new StreamSource(xslFile);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(xslSource);
            transformer.setParameter("selectedSkill", selectedSkill);

            StringWriter writer = new StringWriter();
            transformer.transform(xmlSource, new StreamResult(writer));
            return writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "<html><body><h2>Error while transforming XML with XSL.</h2></body></html>";
        }
    }
}