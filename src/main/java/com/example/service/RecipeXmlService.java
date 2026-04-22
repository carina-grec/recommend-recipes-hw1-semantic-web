package com.example.service;

import com.example.model.Recipe;
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
import javax.xml.xpath.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeXmlService {

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Document document = loadDocument();
            NodeList recipeNodes = document.getElementsByTagName("recipe");

            for (int i = 0; i < recipeNodes.getLength(); i++) {
                Node node = recipeNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    recipes.add(elementToRecipe((Element) node));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public boolean addRecipe(Recipe recipe) {
        try {
            Document document = loadDocument();
            Element root = document.getDocumentElement();

            Element recipeElement = document.createElement("recipe");
            recipeElement.setAttribute("id", getNextRecipeId());

            Element titleElement = document.createElement("title");
            titleElement.setTextContent(recipe.getTitle());
            recipeElement.appendChild(titleElement);

            Element cuisineTypesElement = document.createElement("cuisineTypes");
            for (String cuisine : recipe.getCuisineTypes()) {
                Element cuisineElement = document.createElement("cuisine");
                cuisineElement.setTextContent(cuisine);
                cuisineTypesElement.appendChild(cuisineElement);
            }
            recipeElement.appendChild(cuisineTypesElement);

            Element difficultyElement = document.createElement("difficulty");
            difficultyElement.setTextContent(recipe.getDifficulty());
            recipeElement.appendChild(difficultyElement);

            root.appendChild(recipeElement);

            return validateAndSave(document);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean replaceAllRecipes(List<Recipe> recipes) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("recipes");
            document.appendChild(root);

            for (int i = 0; i < recipes.size(); i++) {
                Recipe recipe = recipes.get(i);

                Element recipeElement = document.createElement("recipe");
                recipeElement.setAttribute("id", String.valueOf(i + 1));

                Element titleElement = document.createElement("title");
                titleElement.setTextContent(recipe.getTitle());
                recipeElement.appendChild(titleElement);

                Element cuisineTypesElement = document.createElement("cuisineTypes");
                for (String cuisine : recipe.getCuisineTypes()) {
                    Element cuisineElement = document.createElement("cuisine");
                    cuisineElement.setTextContent(cuisine);
                    cuisineTypesElement.appendChild(cuisineElement);
                }
                recipeElement.appendChild(cuisineTypesElement);

                Element difficultyElement = document.createElement("difficulty");
                difficultyElement.setTextContent(recipe.getDifficulty());
                recipeElement.appendChild(difficultyElement);

                root.appendChild(recipeElement);
            }

            return validateAndSave(document);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getNextRecipeId() {
        int maxId = 0;
        for (Recipe recipe : getAllRecipes()) {
            try {
                int id = Integer.parseInt(recipe.getId());
                maxId = Math.max(maxId, id);
            } catch (NumberFormatException ignored) {
            }
        }
        return String.valueOf(maxId + 1);
    }

    public List<Recipe> getRecipesByDifficulty(String difficulty) {
        return getRecipesByXPath("/recipes/recipe[difficulty='" + difficulty + "']");
    }

    public List<Recipe> getRecipesByDifficultyAndCuisine(String difficulty, String cuisine) {
        return getRecipesByXPath("/recipes/recipe[difficulty='" + difficulty +
                "' and cuisineTypes/cuisine='" + cuisine + "']");
    }

    public Recipe getRecipeById(String id) {
        List<Recipe> recipes = getRecipesByXPath("/recipes/recipe[@id='" + id + "']");
        return recipes.isEmpty() ? null : recipes.get(0);
    }

    public List<Recipe> getRecipesByCuisine(String cuisine) {
        return getRecipesByXPath("/recipes/recipe[cuisineTypes/cuisine='" + cuisine + "']");
    }

    private List<Recipe> getRecipesByXPath(String expression) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            Document document = loadDocument();
            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    recipes.add(elementToRecipe((Element) node));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recipes;
    }

    private Document loadDocument() throws Exception {
        File file = XmlFileUtil.getRecipesXmlFile();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }

    private Recipe elementToRecipe(Element recipeElement) {
        String id = recipeElement.getAttribute("id");
        String title = recipeElement.getElementsByTagName("title").item(0).getTextContent();
        String difficulty = recipeElement.getElementsByTagName("difficulty").item(0).getTextContent();

        List<String> cuisineTypes = new ArrayList<>();
        NodeList cuisineNodes = recipeElement.getElementsByTagName("cuisine");
        for (int j = 0; j < cuisineNodes.getLength(); j++) {
            cuisineTypes.add(cuisineNodes.item(j).getTextContent());
        }

        return new Recipe(id, title, cuisineTypes, difficulty);
    }

    private boolean validateAndSave(Document document) {
        try {
            File xmlFile = XmlFileUtil.getRecipesXmlFile();
            File xsdFile = XmlFileUtil.getRecipesXsdFile();

            File tempFile = File.createTempFile("recipes-temp", ".xml");
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