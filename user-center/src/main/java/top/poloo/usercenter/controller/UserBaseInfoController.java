package top.poloo.usercenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.poloo.usercenter.service.UserBaseInfoService;

/**
 * @Author: polo
 * @Date: 2021/10/21 16:42
 */
@RestController
@RequestMapping(value = "api/userBaseInfo")
public class UserBaseInfoController {
    @Autowired
    UserBaseInfoService baseInfoService;
    @GetMapping("getId")
    public String get(String id){
        return baseInfoService.getById(id).getUserName();

    }
}
