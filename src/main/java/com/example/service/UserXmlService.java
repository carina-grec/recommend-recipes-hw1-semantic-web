package com.example.service;

import com.example.model.User;
import com.example.util.XmlFileUtil;
import com.example.util.XmlValidationUtil;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserXmlService {

    private static final String XML_PATH = "data/users.xml";
    private static final String XSD_PATH = "data/users.xsd";

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try {
            Document document = loadDocument();
            NodeList userNodes = document.getElementsByTagName("user");

            for (int i = 0; i < userNodes.getLength(); i++) {
                Node node = userNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    users.add(elementToUser((Element) node));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public User getFirstUser() {
        List<User> users = getAllUsers();
        return users.isEmpty() ? null : users.get(0);
    }

    public User getUserById(String id) {
        for (User user : getAllUsers()) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public boolean addUser(User user) {
        try {
            Document document = loadDocument();
            Element root = document.getDocumentElement();

            Element userElement = document.createElement("user");
            userElement.setAttribute("id", getNextUserId());

            Element nameElement = document.createElement("name");
            nameElement.setTextContent(user.getName());
            userElement.appendChild(nameElement);

            Element surnameElement = document.createElement("surname");
            surnameElement.setTextContent(user.getSurname());
            userElement.appendChild(surnameElement);

            Element skillElement = document.createElement("cookingSkillLevel");
            skillElement.setTextContent(user.getCookingSkillLevel());
            userElement.appendChild(skillElement);

            Element cuisineElement = document.createElement("preferredCuisineType");
            cuisineElement.setTextContent(user.getPreferredCuisineType());
            userElement.appendChild(cuisineElement);

            root.appendChild(userElement);

            return validateAndSave(document);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNextUserId() {
        int maxId = 0;
        for (User user : getAllUsers()) {
            try {
                int id = Integer.parseInt(user.getId());
                maxId = Math.max(maxId, id);
            } catch (NumberFormatException ignored) {
            }
        }
        return String.valueOf(maxId + 1);
    }

    private Document loadDocument() throws Exception {
        File file = XmlFileUtil.getUsersXmlFile();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }

    private User elementToUser(Element userElement) {
        String id = userElement.getAttribute("id");
        String name = userElement.getElementsByTagName("name").item(0).getTextContent();
        String surname = userElement.getElementsByTagName("surname").item(0).getTextContent();
        String cookingSkillLevel = userElement.getElementsByTagName("cookingSkillLevel").item(0).getTextContent();
        String preferredCuisineType = userElement.getElementsByTagName("preferredCuisineType").item(0).getTextContent();

        return new User(id, name, surname, cookingSkillLevel, preferredCuisineType);
    }

    private boolean validateAndSave(Document document) {
        try {
            File xmlFile = XmlFileUtil.getUsersXmlFile();
            File xsdFile = XmlFileUtil.getUsersXsdFile();

            File tempFile = File.createTempFile("users-temp", ".xml");
            saveDocument(document, tempFile);

            boolean valid = XmlValidationUtil.validateXml(tempFile, xsdFile);
            if (!valid) {
                tempFile.delete();
                return false;
            }

            saveDocument(document, xmlFile);
            tempFile.delete();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void saveDocument(Document document, File file) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(document), new StreamResult(file));
    }
}