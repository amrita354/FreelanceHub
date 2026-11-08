
package com.freelancehub.controller;
import java.util.List;
import com.freelancehub.model.Proposal;
import com.freelancehub.model.Freelancer;
import com.freelancehub.model.Project;
import com.freelancehub.model.User;
import com.freelancehub.repository.ProjectRepository;
import com.freelancehub.service.ProposalService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProposalController {

    private final ProposalService proposalService;
    private final ProjectRepository projectRepository;

    public ProposalController(
            ProposalService proposalService,
            ProjectRepository projectRepository) {
        this.proposalService = proposalService;
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects/{projectId}/proposal")
    public String showProposalForm(
            @PathVariable Long projectId,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Freelancer)) {
            return "redirect:/login";
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Project not found"));

        if (!"OPEN".equals(project.getStatus())) {
            return "redirect:/projects";
        }

        model.addAttribute("project", project);
        return "proposal-form";
    }

    @PostMapping("/projects/{projectId}/proposal")
    public String submitProposal(
            @PathVariable Long projectId,
            @RequestParam String coverLetter,
            @RequestParam double proposedBudget,
            @RequestParam int estimatedDays,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Freelancer)) {
            return "redirect:/login";
        }

        try {
            proposalService.submitProposal(
                    projectId,
                    (Freelancer) user,
                    coverLetter,
                    proposedBudget,
                    estimatedDays
            );

            return "redirect:/projects";

        } catch (IllegalArgumentException e) {
            Project project = projectRepository.findById(projectId)
                    .orElse(null);

            model.addAttribute("project", project);
            model.addAttribute("error", e.getMessage());

            return "proposal-form";
        }
    }
    @GetMapping("/freelancer/proposals")
public String myProposals(HttpSession session, Model model) {

    User user = (User) session.getAttribute("loggedInUser");

    if (!(user instanceof Freelancer)) {
        return "redirect:/login";
    }

    Freelancer freelancer = (Freelancer) user;

    model.addAttribute(
            "proposals",
            proposalService.getFreelancerProposals(freelancer.getId())
    );

    return "freelancer-proposals";
}
}
