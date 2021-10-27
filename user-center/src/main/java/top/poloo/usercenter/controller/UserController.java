package top.poloo.usercenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.poloo.common.com.R;
import top.poloo.usercenter.feign.UserFeign;
import top.poloo.usercenter.service.UserBaseInfoService;

/**
 * @Author: polo
 * @Date: 2021/10/21 16:42
 */
@RestController
@RequestMapping(value = UserFeign.BASE_URL)
public class UserController implements UserFeign {
    @Autowired
    UserBaseInfoService baseInfoService;


    @Override
    @PostMapping("/checkUserNameAndPassword")
    public R<?> checkUserNameAndPassword(String userName, String password) {
        return baseInfoService.checkUserNameAndPassword(userName,password);
    }

    @Override
    @PostMapping("/addUser")
    public R<?> addUser(String userName, String password) {
        return baseInfoService.addUser(userName,password);
    }
}
