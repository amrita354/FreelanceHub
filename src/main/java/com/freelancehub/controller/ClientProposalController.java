package com.freelancehub.controller;

import com.freelancehub.model.Client;
import com.freelancehub.model.User;
import com.freelancehub.service.ProposalService;
import com.freelancehub.service.ProposalWorkflowService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClientProposalController {

    private final ProposalService proposalService;
    private final ProposalWorkflowService proposalWorkflowService;

    public ClientProposalController(
            ProposalService proposalService,
            ProposalWorkflowService proposalWorkflowService) {

        this.proposalService = proposalService;
        this.proposalWorkflowService = proposalWorkflowService;
    }

    // View proposals received by the client
    @GetMapping("/client/proposals")
    public String viewProposals(
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Client)) {
            return "redirect:/login";
        }

        Client client = (Client) user;

        model.addAttribute(
                "proposals",
                proposalService.getClientProposals(client.getId())
        );

        return "client-proposals";
    }

    // Accept or reject a proposal
    @PostMapping("/client/proposals/{proposalId}/status")
    public String updateStatus(
            @PathVariable Long proposalId,
            @RequestParam String status,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Client)) {
            return "redirect:/login";
        }

        Client client = (Client) user;

        try {
            proposalWorkflowService.reviewProposal(
                    proposalId,
                    client.getId(),
                    status
            );

        } catch (IllegalArgumentException exception) {
            System.out.println("Proposal/Contract Error: "
                    + exception.getMessage());
        }

        return "redirect:/client/proposals";
    }
}