package me.huangduo.hms.controller;

//import me.huangduo.hms.User;
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
//        User user = new User();
//        user.setName("huang");
//        System.out.println(user.getName());
        return Arrays.asList("huang duo", "zhang san");
    }
}
