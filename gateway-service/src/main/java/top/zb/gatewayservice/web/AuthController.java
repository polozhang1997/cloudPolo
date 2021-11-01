package top.zb.gatewayservice.web;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import top.poloo.common.com.R;
import top.poloo.common.constant.Rcode;
import top.poloo.usercenter.feign.UserFeign;
import top.poloo.common.com.AuthUser;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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


    //登录验证
    @PostMapping
    public Map<String,Object> login(ServerWebExchange exchange,
                      WebSession session,
                      String userName,
                      String password){
        R<?> login = userFeign.login(userName, password);
        if (Rcode.FAIL_CODE.equals(login.getCode())){
            Map<String,Object> map = new HashMap<>();
            map.put("code",login.getCode());
            map.put("message", login.getMsg());
            return map;
        }
        //账户密码验证成功
        if (Rcode.SUCCESS_CODE.equals(login.getCode())){
            Object data = login.getData();
            AuthUser authUser = JSONObject.parseObject(JSONObject.toJSONString(data), AuthUser.class);
            //认证  //Authentication子类
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(authUser, null, Collections.emptyList());
            serverSecurityContextRepository.save(exchange, new SecurityContextImpl(token));

            Map<String,Object> map = new HashMap<>(16);
            map.put("token",authUser.getJwtToken());
            map.put("sessionId",session.getId());
            return  map;

        }
        return null;

    }

}
