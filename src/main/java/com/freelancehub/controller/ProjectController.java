
package com.freelancehub.controller;

import com.freelancehub.model.Client;
import com.freelancehub.model.Project;
import com.freelancehub.model.User;
import com.freelancehub.service.ProjectService;

import jakarta.servlet.http.HttpSession;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects/new")
    public String showProjectForm(HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Client)) {
            return "redirect:/login";
        }

        return "project-form";
    }

    @PostMapping("/projects/create")
    public String createProject(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam double budget,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate deadline,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Client)) {
            return "redirect:/login";
        }

        try {
            Project project = projectService.createProject(
                    title, description, budget, deadline, (Client) user
            );

            model.addAttribute(
                    "success", "Project posted successfully!"
            );

            return "redirect:/dashboard";

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "project-form";
        }
    }

@GetMapping("/projects")
public String viewProjects(HttpSession session, Model model) {

    User user = (User) session.getAttribute("loggedInUser");

    if (!(user instanceof com.freelancehub.model.Freelancer)) {
        return "redirect:/login";
    }

    model.addAttribute(
            "projects", projectService.getOpenProjects()
    );

    return "projects";
}

}
