
package com.freelancehub.controller;

import com.freelancehub.model.Client;
import com.freelancehub.model.Freelancer;
import com.freelancehub.model.User;
import com.freelancehub.service.ContractService;
import com.freelancehub.service.MilestoneService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class MilestoneController {

    private final MilestoneService milestoneService;
    private final ContractService contractService;

    public MilestoneController(
            MilestoneService milestoneService,
            ContractService contractService) {

        this.milestoneService = milestoneService;
        this.contractService = contractService;
    }

    // View milestones of a contract
    @GetMapping("/contracts/{contractId}/milestones")
    public String viewMilestones(
            @PathVariable Long contractId,
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        var contract = contractService.getAllContracts()
                .stream()
                .filter(c -> c.getId().equals(contractId))
                .findFirst()
                .orElse(null);

        if (contract == null) {
            return "redirect:/contracts";
        }

        boolean authorized = false;

        if (user instanceof Client) {
            Client client = (Client) user;
            authorized = contract.getProposal()
                    .getProject().getClient().getId()
                    .equals(client.getId());

        } else if (user instanceof Freelancer) {
            Freelancer freelancer = (Freelancer) user;
            authorized = contract.getProposal()
                    .getFreelancer().getId()
                    .equals(freelancer.getId());
        }

        if (!authorized) {
            return "redirect:/contracts";
        }

        model.addAttribute("contract", contract);
        model.addAttribute(
                "milestones",
                milestoneService.getMilestonesByContract(contractId)
        );

        model.addAttribute("isClient", user instanceof Client);

        return "milestones";
    }

    // Create milestone (Client only)
    @PostMapping("/contracts/{contractId}/milestones/create")
    public String createMilestone(
            @PathVariable Long contractId,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam double amount,
            @RequestParam LocalDate dueDate,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Client)) {
            return "redirect:/login";
        }

        Client client = (Client) user;

        var contract = contractService.getAllContracts()
                .stream()
                .filter(c -> c.getId().equals(contractId))
                .findFirst()
                .orElse(null);

        if (contract == null ||
                !contract.getProposal().getProject()
                        .getClient().getId().equals(client.getId())) {
            return "redirect:/contracts";
        }

        try {
            milestoneService.createMilestone(
                    contractId, title, description, amount, dueDate
            );
        } catch (IllegalArgumentException exception) {
            System.out.println("Milestone Error: "
                    + exception.getMessage());
        }

        return "redirect:/contracts/" + contractId + "/milestones";
    }

    // Update milestone status (Freelancer only)
    @PostMapping("/milestones/{milestoneId}/status")
    public String updateStatus(
            @PathVariable Long milestoneId,
            @RequestParam String status,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Freelancer)) {
            return "redirect:/login";
        }

        Freelancer freelancer = (Freelancer) user;

        try {
            var milestone = milestoneService.getMilestoneById(milestoneId);

            if (!milestone.getContract().getProposal()
                    .getFreelancer().getId().equals(freelancer.getId())) {
                return "redirect:/contracts";
            }

            milestoneService.updateMilestoneStatus(milestoneId, status);

        } catch (IllegalArgumentException exception) {
            System.out.println("Milestone Error: "
                    + exception.getMessage());
        }

        return "redirect:/contracts";
    }
}
