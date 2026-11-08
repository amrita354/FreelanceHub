
package com.freelancehub.controller;

import com.freelancehub.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String role,
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String skills,
            @RequestParam(required = false, defaultValue = "0")
            int experienceYears,
            Model model) {

        try {
            if (role.equalsIgnoreCase("CLIENT")) {
                userService.registerClient(
                        name, email, password, companyName
                );
            } else if (role.equalsIgnoreCase("FREELANCER")) {
                userService.registerFreelancer(
                        name, email, password, skills, experienceYears
                );
            } else {
                model.addAttribute("error", "Invalid account type");
                return "register";
            }

            model.addAttribute(
                    "success", "Registration successful!"
            );

            return "register";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
