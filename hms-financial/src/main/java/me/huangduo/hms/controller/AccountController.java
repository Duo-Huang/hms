package me.huangduo.hms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@Slf4j
public class AccountController {


    @GetMapping
    public List<String> getUsers() {
        log.info("get account...");
        return Arrays.asList("huang duo", "zhang san");
    }
}
