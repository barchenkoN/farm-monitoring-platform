package com.farmmonitoring.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RootController {
    
    @GetMapping
    public String redirectToDashboard() {
        return "redirect:/web";
    }
}
