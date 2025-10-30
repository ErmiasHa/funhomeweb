package com.funhomebase.funhomeweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Laddar index.html från src/main/resources/static/
        return "redirect:/index.html";
    }
}
