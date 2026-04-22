package com.example.service;

import com.example.util.XmlFileUtil;
import com.example.util.XmlValidationUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class StartupValidationService {

    @PostConstruct
    public void validateXmlFiles() {
        boolean recipesValid = XmlValidationUtil.validateXml(
                XmlFileUtil.getRecipesXmlFile(),
                XmlFileUtil.getRecipesXsdFile()
        );

        boolean usersValid = XmlValidationUtil.validateXml(
                XmlFileUtil.getUsersXmlFile(),
                XmlFileUtil.getUsersXsdFile()
        );

        System.out.println("Recipes XML valid: " + recipesValid);
        System.out.println("Users XML valid: " + usersValid);
    }
}