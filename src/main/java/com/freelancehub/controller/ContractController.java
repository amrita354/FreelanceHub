
package com.freelancehub.controller;

import com.freelancehub.model.Client;
import com.freelancehub.model.Freelancer;
import com.freelancehub.model.User;
import com.freelancehub.service.ContractService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/contracts")
    public String viewContracts(
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        if (user instanceof Client) {

            Client client = (Client) user;

            model.addAttribute(
                    "contracts",
                    contractService.getContractsForClient(client.getId())
            );

        } else if (user instanceof Freelancer) {

            Freelancer freelancer = (Freelancer) user;

            model.addAttribute(
                    "contracts",
                    contractService.getContractsForFreelancer(
                            freelancer.getId())
            );

        } else {
            return "redirect:/login";
        }

        return "contracts";
    }
}
