package me.huangduo.hms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {


    @GetMapping
    public List<String> getUsers() {
        return Arrays.asList("huang duo", "zhang san");
    }
}
