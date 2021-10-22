package top.zb.gatewayservice.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Author: polo
 * @Date: 2021/10/21 17:12
 */
@Configuration
public class DynamicRoute {

    /**
     * nacos配置中心初始化
     * @return
     */
    @Bean
    public ConfigService initConfigService(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR,GatewayConfig.NACOS_SERVER_ADDR);
        try {
            return NacosFactory.createConfigService(properties);
        } catch (NacosException e) {
            e.printStackTrace();
            return null;
        }
    }
}
