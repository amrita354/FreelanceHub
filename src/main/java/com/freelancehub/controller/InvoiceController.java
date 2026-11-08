
package com.freelancehub.controller;

import com.freelancehub.model.Client;
import com.freelancehub.model.Freelancer;
import com.freelancehub.model.User;
import com.freelancehub.service.InvoiceService;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    // View invoices
    @GetMapping("/invoices")
    public String viewInvoices(
            HttpSession session,
            Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user instanceof Client) {

            Client client = (Client) user;

            model.addAttribute(
                    "invoices",
                    invoiceService.getClientInvoices(client.getId())
            );

            model.addAttribute("isClient", true);

        } else if (user instanceof Freelancer) {

            Freelancer freelancer = (Freelancer) user;

            model.addAttribute(
                    "invoices",
                    invoiceService.getFreelancerInvoices(
                            freelancer.getId())
            );

            model.addAttribute("isClient", false);

        } else {
            return "redirect:/login";
        }

        return "invoices";
    }

    // Generate invoice (Freelancer only)
    @PostMapping("/invoices/generate/{milestoneId}")
    public String generateInvoice(
            @PathVariable Long milestoneId,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Freelancer)) {
            return "redirect:/login";
        }

        Freelancer freelancer = (Freelancer) user;

        try {
            invoiceService.generateInvoice(milestoneId, freelancer.getId());
        } catch (IllegalArgumentException exception) {
            System.out.println("Invoice Error: "
                    + exception.getMessage());
        }

        return "redirect:/invoices";
    }

    // Simulate payment (Client only)
    @PostMapping("/invoices/pay/{invoiceId}")
    public String makePayment(
            @PathVariable Long invoiceId,
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");

        if (!(user instanceof Client)) {
            return "redirect:/login";
        }

        Client client = (Client) user;

        try {
            invoiceService.makePayment(invoiceId, client.getId());
        } catch (IllegalArgumentException exception) {
            System.out.println("Payment Error: "
                    + exception.getMessage());
        }

        return "redirect:/invoices";
    }
}
