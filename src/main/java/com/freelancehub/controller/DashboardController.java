
package com.freelancehub.controller;

import com.freelancehub.model.User;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        if (user.getAccountType().equals("CLIENT")) {
            return "client-dashboard";
        }

        if (user.getAccountType().equals("FREELANCER")) {
            return "freelancer-dashboard";
        }

        return "redirect:/login";
    }
}
