package top.zb.gatewayservice.config.matcher;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.zb.gatewayservice.config.White;
import top.zb.gatewayservice.config.WhiteApiConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * 暂且未用
 * @Author: polo
 * @Date: 2021/10/26 17:08
 */
@Deprecated
@Configuration
public class WhiteApiMatcher  implements ServerWebExchangeMatcher {

    @Autowired
    ConfigService configService;

    private List<ServerWebExchangeMatcher> serverWebExchangeMatcherList;

    @Override
    public Mono<MatchResult> matches(ServerWebExchange serverWebExchange) {
        if (serverWebExchangeMatcherList == null){
            setServerWebExchangeMatcherList(getWhiteApiListFromNacos());
        }

        return Flux.fromIterable(serverWebExchangeMatcherList)
                .flatMap(m -> m.matches(serverWebExchange))
                .filter(MatchResult::isMatch)
                .next()
                .switchIfEmpty(MatchResult.notMatch());

    }

    public void setServerWebExchangeMatcherList(List<String> whiteList){
        this.serverWebExchangeMatcherList = new ArrayList<>(whiteList.size());
        for (String pattern : whiteList) {
            this.serverWebExchangeMatcherList.add(new PathPatternParserServerWebExchangeMatcher(pattern, null));
        }


    }

    /**
     * 获取nacos白名单的列表
     * @return List<String>
     */
    private List<String> getWhiteApiListFromNacos(){

        String config = null;
        try {
            config = configService.getConfig(WhiteApiConfig.DATA_ID, WhiteApiConfig.GROUP, 100000);
        } catch (NacosException e) {
            e.printStackTrace();
        }
        White white = JSONObject.parseObject(config, White.class);
        return white.getWhiteApiListNacos();

    }
    @Override
    public String toString() {
        return "WhiteApiMatcher{" +
                "matchers=" + serverWebExchangeMatcherList +
                '}';
    }


}
