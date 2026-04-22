package com.example.controller;

import com.example.model.UserForm;
import com.example.service.UserXmlService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private final UserXmlService userXmlService;

    public UserController(UserXmlService userXmlService) {
        this.userXmlService = userXmlService;
    }

    @ModelAttribute("difficultyOptions")
    public List<String> difficultyOptions() {
        return List.of("Beginner", "Intermediate", "Advanced");
    }

    @ModelAttribute("cuisineOptions")
    public List<String> cuisineOptions() {
        return List.of("Italian", "Asian", "Indian", "Mexican", "French", "Mediterranean", "American", "European");
    }

    @GetMapping("/users/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "add-user";
    }

    @PostMapping("/users/add")
    public String addUser(@ModelAttribute UserForm userForm,
                          RedirectAttributes redirectAttributes,
                          Model model) {

        if (userForm.getName() == null || userForm.getName().trim().isEmpty()) {
            model.addAttribute("error", "Name cannot be empty.");
            return "add-user";
        }

        if (userForm.getSurname() == null || userForm.getSurname().trim().isEmpty()) {
            model.addAttribute("error", "Surname cannot be empty.");
            return "add-user";
        }

        if (userForm.getCookingSkillLevel() == null || userForm.getCookingSkillLevel().isBlank()) {
            model.addAttribute("error", "Please select a cooking skill level.");
            return "add-user";
        }

        if (userForm.getPreferredCuisineType() == null || userForm.getPreferredCuisineType().isBlank()) {
            model.addAttribute("error", "Please select a preferred cuisine type.");
            return "add-user";
        }

        boolean saved = userXmlService.addUser(userForm.toUser());

        if (!saved) {
            model.addAttribute("error", "User could not be saved because XML validation failed.");
            return "add-user";
        }

        redirectAttributes.addFlashAttribute("success", "User added successfully.");
        return "redirect:/";
    }
}