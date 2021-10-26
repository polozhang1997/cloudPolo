package top.poloo.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: polo
 * @Date: 2021/10/22 15:15
 */
@RestController
@RequestMapping("/auth")
public class LoginController {

    @GetMapping("/login")
    public String login(){
        return "login";
    }

}
