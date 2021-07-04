package com.myportfolio.eightg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/restricted")
    public String test() {

        ObjectMapper objectMapper = new ObjectMapper();

        DefaultOAuth2User object = (DefaultOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = (String) object.getAttributes().get("email");

        return object.toString();
    }
}
