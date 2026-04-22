package com.example.controller;

import com.example.model.Recipe;
import com.example.model.RecipeForm;
import com.example.service.RecipeXmlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeXmlService recipeXmlService;

    public RecipeController(RecipeXmlService recipeXmlService) {
        this.recipeXmlService = recipeXmlService;
    }

    @ModelAttribute("cuisineOptions")
    public List<String> cuisineOptions() {
        return List.of("Italian", "Asian", "Indian", "Mexican", "French", "Mediterranean", "American", "European");
    }

    @ModelAttribute("difficultyOptions")
    public List<String> difficultyOptions() {
        return List.of("Beginner", "Intermediate", "Advanced");
    }

    @GetMapping("/add")
    public String showAddRecipeForm(Model model) {
        model.addAttribute("recipeForm", new RecipeForm());
        return "add-recipe";
    }

    @PostMapping("/add")
    public String addRecipe(@ModelAttribute RecipeForm recipeForm,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (recipeForm.getTitle() == null || recipeForm.getTitle().trim().isEmpty()) {
            model.addAttribute("error", "Title cannot be empty.");
            return "add-recipe";
        }

        if (recipeForm.getCuisine1() == null || recipeForm.getCuisine2() == null ||
                recipeForm.getCuisine1().isBlank() || recipeForm.getCuisine2().isBlank() ||
                recipeForm.getCuisine1().equals(recipeForm.getCuisine2())) {
            model.addAttribute("error", "Please select 2 different cuisine types.");
            return "add-recipe";
        }

        if (recipeForm.getDifficulty() == null || recipeForm.getDifficulty().isBlank()) {
            model.addAttribute("error", "Please select a difficulty.");
            return "add-recipe";
        }

        boolean saved = recipeXmlService.addRecipe(recipeForm.toRecipe());

        if (!saved) {
            model.addAttribute("error", "Recipe could not be saved because XML validation failed.");
            return "add-recipe";
        }

        redirectAttributes.addFlashAttribute("success", "Recipe added successfully.");
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String recipeDetails(@PathVariable String id, Model model) {
        Recipe recipe = recipeXmlService.getRecipeById(id);

        if (recipe == null) {
            model.addAttribute("error", "Recipe not found.");
        } else {
            model.addAttribute("recipe", recipe);
        }

        return "recipe-details";
    }

    @GetMapping("/filter")
    public String filterByCuisine(@RequestParam(required = false) String cuisine, Model model) {
        model.addAttribute("selectedCuisine", cuisine);

        if (cuisine != null && !cuisine.isBlank()) {
            model.addAttribute("recipes", recipeXmlService.getRecipesByCuisine(cuisine));
        }

        return "filter-recipes";
    }
}