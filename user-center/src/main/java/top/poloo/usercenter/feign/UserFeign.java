package top.poloo.usercenter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.poloo.common.com.R;


/**
 * @Author: polo
 * @Date: 2021/10/27 17:25
 */

@FeignClient(value = "user-center")   //必须是application定义的name
public interface UserFeign {

    String BASE_URL = "/user";

    /**
     * 验证用户名和密码
     * @param userName - 用户名
     * @param password - 密码
     * @return - R
     */
    @PostMapping(BASE_URL+"/checkUserNameAndPassword")
    R<?> login(@RequestParam("userName") String userName,@RequestParam("password") String password);

    /**
     * 添加用户
     * @param userName - 用户名
     * @param password - 密码
     * @return - R
     */
    @PostMapping(BASE_URL+"/addUser")
    R<?> addUser(@RequestParam("userName")String userName,@RequestParam("password") String password);

}
