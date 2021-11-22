package top.zb.gatewayservice.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import top.poloo.common.com.R;
import top.poloo.common.com.Rcode;
import top.poloo.usercenter.feign.UserFeign;
import top.poloo.common.com.AuthUser;
import top.zb.gatewayservice.entity.Login;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: polo
 * @Date: 2021/10/29 15:37
 */
@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private UserFeign userFeign;
    //为啥不能自动注入？
    ServerSecurityContextRepository serverSecurityContextRepository = new WebSessionServerSecurityContextRepository();



    @PostMapping
    public R login(ServerWebExchange exchange,
                   WebSession session,
                   Login loginBody){
        String password = loginBody.getPassword();
        String userName = loginBody.getUserName();
        R login = userFeign.login(userName, password);
        if (Rcode.FAILED.getCode().equals(login.getCode())){
            return login;
        }

        if (Rcode.SUCCESS.getCode().equals(login.getCode())){
            Object data = login.getData();
            AuthUser authUser = JSONObject.parseObject(JSONObject.toJSONString(data), AuthUser.class);
            //认证  //Authentication子类
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authUser, null, Collections.emptyList());
            serverSecurityContextRepository.save(exchange, new SecurityContextImpl(token));

            Map<String,Object> map = new HashMap<>(16);
            map.put("token",authUser.getJwtToken());
            map.put("sessionId",session.getId());
            return  R.success(map);
        }
        return null;

    }


}
