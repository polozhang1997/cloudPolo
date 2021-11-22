package top.zb.gatewayservice.config;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import java.util.concurrent.Executor;

/**
 * @Author: polo
 * @Date: 2021/10/22 16:11
 */
@Component
@Slf4j
public class WhiteApiListener {
    @Autowired
    ConfigService configService;

    /**
     * 获取nacos上的白名单api并监听更新
     */
    @EventListener(ApplicationReadyEvent.class)
    public void whiteApiSet(){
        try {
            String data = configService.getConfigAndSignListener(WhiteApiConfig.DATA_ID, WhiteApiConfig.GROUP, 100000, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }
                //监听变更
                @Override
                public void receiveConfigInfo(String data) {
                    White white = JSONObject.parseObject(data, White.class);
                    WhiteApiConfig.whiteApiList = white.getWhiteApiListNacos();
                    WhiteApiConfig.whiteIpList = white.getWhiteIpListNacos();
                    log.info("更新后的白名单Route有：{}",Convert.toStr(WhiteApiConfig.whiteApiList));
                    log.info("更新后的白名单Ip有：{}",Convert.toStr(WhiteApiConfig.whiteIpList));
                }
            });
            White white = JSONObject.parseObject(data, White.class);
            WhiteApiConfig.whiteApiList = white.getWhiteApiListNacos();
            WhiteApiConfig.whiteIpList = white.getWhiteIpListNacos();
            log.info("默认配置的白名单Route有：{}",Convert.toStr(WhiteApiConfig.whiteApiList));
            log.info("默认配置的白名单Ip有：{}",Convert.toStr(WhiteApiConfig.whiteIpList));
        } catch (NacosException e) {
            log.warn("请在nacos编写dataId为：{}，group为：{}的相关配置文件",WhiteApiConfig.DATA_ID,WhiteApiConfig.GROUP);
        }catch (JSONException e){
            log.error("nacos配置文件编写错误，导致转换异常，请按照转换实体编写");
        }
    }

}
