package com.example.controller;

import com.example.model.User;
import com.example.service.UserXmlService;
import com.example.service.XslTransformationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class XslController {

    private final XslTransformationService xslTransformationService;
    private final UserXmlService userXmlService;

    public XslController(XslTransformationService xslTransformationService,
                         UserXmlService userXmlService) {
        this.xslTransformationService = xslTransformationService;
        this.userXmlService = userXmlService;
    }

    @GetMapping("/recipes/xsl")
    public String showRecipesUsingXsl(@RequestParam(required = false) String userId, Model model) {
        User selectedUser = (userId != null && !userId.isBlank())
                ? userXmlService.getUserById(userId)
                : userXmlService.getFirstUser();

        String selectedSkill = selectedUser != null ? selectedUser.getCookingSkillLevel() : "Beginner";
        String transformedHtml = xslTransformationService.transformRecipesXml(selectedSkill);

        model.addAttribute("transformedHtml", transformedHtml);
        model.addAttribute("users", userXmlService.getAllUsers());
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("selectedUser", selectedUser);

        return "recipes-xsl";
    }
}