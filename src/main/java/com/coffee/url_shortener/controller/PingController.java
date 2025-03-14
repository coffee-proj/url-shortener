package com.coffee.url_shortener.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @CrossOrigin
    @GetMapping("/ping")
    public void ping() {
    }
}
