package com.treadingPlatformApplication.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Health {

    @GetMapping
    public String hy(){
        return "I Am Fine";
    }
}
