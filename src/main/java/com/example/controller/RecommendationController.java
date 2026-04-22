package com.example.controller;

import com.example.model.User;
import com.example.service.RecipeXmlService;
import com.example.service.UserXmlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RecommendationController {

    private final RecipeXmlService recipeXmlService;
    private final UserXmlService userXmlService;

    public RecommendationController(RecipeXmlService recipeXmlService, UserXmlService userXmlService) {
        this.recipeXmlService = recipeXmlService;
        this.userXmlService = userXmlService;
    }

    @GetMapping("/recommend/skill")
    public String recommendBySkill(@RequestParam(required = false) String userId, Model model) {
        User selectedUser = getSelectedUser(userId);

        if (selectedUser == null) {
            model.addAttribute("error", "No users found in XML.");
            model.addAttribute("users", userXmlService.getAllUsers());
            return "recommend-skill";
        }

        model.addAttribute("users", userXmlService.getAllUsers());
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("user", selectedUser);
        model.addAttribute("recipes",
                recipeXmlService.getRecipesByDifficulty(selectedUser.getCookingSkillLevel()));

        return "recommend-skill";
    }

    @GetMapping("/recommend/skill-cuisine")
    public String recommendBySkillAndCuisine(@RequestParam(required = false) String userId, Model model) {
        User selectedUser = getSelectedUser(userId);

        if (selectedUser == null) {
            model.addAttribute("error", "No users found in XML.");
            model.addAttribute("users", userXmlService.getAllUsers());
            return "recommend-skill-cuisine";
        }

        model.addAttribute("users", userXmlService.getAllUsers());
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("user", selectedUser);
        model.addAttribute("recipes",
                recipeXmlService.getRecipesByDifficultyAndCuisine(
                        selectedUser.getCookingSkillLevel(),
                        selectedUser.getPreferredCuisineType()
                ));

        return "recommend-skill-cuisine";
    }

    private User getSelectedUser(String userId) {
        if (userId != null && !userId.isBlank()) {
            return userXmlService.getUserById(userId);
        }
        return userXmlService.getFirstUser();
    }
}