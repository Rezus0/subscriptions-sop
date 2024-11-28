package com.example.subscriptions_sop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ws")
public class WSController {
    @GetMapping()
    public String getMessages() {
        return "ws";
    }
}
