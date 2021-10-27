package top.poloo.auth.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.poloo.auth.feign.AuthFeign;


/**
 * @Author: polo
 * @Date: 2021/10/22 15:15
 */
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthFeign {


}
