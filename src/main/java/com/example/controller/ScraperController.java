package com.example.controller;

import com.example.model.Recipe;
import com.example.service.BbcRecipeScraperService;
import com.example.service.RecipeXmlService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ScraperController {

    private final BbcRecipeScraperService scraperService;
    private final RecipeXmlService recipeXmlService;

    public ScraperController(BbcRecipeScraperService scraperService,
                             RecipeXmlService recipeXmlService) {
        this.scraperService = scraperService;
        this.recipeXmlService = recipeXmlService;
    }

    @GetMapping("/recipes/scrape")
    public String scrapeRecipes(RedirectAttributes redirectAttributes) {
        List<Recipe> scrapedRecipes = scraperService.scrapeRecipes();

        if (scrapedRecipes.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Scraping failed. No recipe titles could be extracted.");
            return "redirect:/";
        }

        if (scrapedRecipes.size() < 20) {
            redirectAttributes.addFlashAttribute("error", "Scraping found only " + scrapedRecipes.size() + " recipes.");
            return "redirect:/";
        }

        boolean saved = recipeXmlService.replaceAllRecipes(scrapedRecipes);

        if (saved) {
            redirectAttributes.addFlashAttribute("success", "ghfvh");
        } else {
            redirectAttributes.addFlashAttribute("error", "Scraping worked, but saving failed because XML validation failed.");
        }

        return "redirect:/";
    }
}