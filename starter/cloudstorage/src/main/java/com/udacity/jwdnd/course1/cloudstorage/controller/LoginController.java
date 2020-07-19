package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.tags.Param;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginView() {
        return "/login";
    }

    @RequestMapping("/login-error")
    public String loginErrorView(Model model) {
        model.addAttribute("loginError", true);
        return "/login";
    }

    @RequestMapping("/logout")
    public String logoutView(Model model) {
        model.addAttribute("logout", true);
        return "/login";
    }
}
