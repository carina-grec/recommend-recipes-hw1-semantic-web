package com.example.controller;

import com.example.service.RecipeXmlService;
import com.example.service.UserXmlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final RecipeXmlService recipeXmlService;
    private final UserXmlService userXmlService;

    public HomeController(RecipeXmlService recipeXmlService, UserXmlService userXmlService) {
        this.recipeXmlService = recipeXmlService;
        this.userXmlService = userXmlService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("recipes", recipeXmlService.getAllRecipes());
        model.addAttribute("users", userXmlService.getAllUsers());
        model.addAttribute("firstUser", userXmlService.getFirstUser());
        return "index";
    }
}