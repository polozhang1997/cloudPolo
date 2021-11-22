package top.zb.gatewayservice.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @Author: polo
 * @Date: 2021/10/21 17:06
 */
@Configuration
@Slf4j
public class DynamicGatewayConfig {

    public static String GATEWAY_CONFIG_DATA_ID;

    public static String GATEWAY_CONFIG_GROUP;

    public static String NACOS_SERVER_ADDR;

    @Value("${spring.cloud.nacos.discovery.server-addr}")
    public void setNacosServerAddr(String nacosServerAddr){
        NACOS_SERVER_ADDR = nacosServerAddr;
    }

    @Value("${polo.gateway.dateId}")
    public void setGatewayConfigDataId(String gatewayConfigDataId){
        GATEWAY_CONFIG_DATA_ID = gatewayConfigDataId;
    }

    @Value("${polo.gateway.group}")
    public void setGatewayConfigGroup(String gatewayConfigGroup){
        GATEWAY_CONFIG_GROUP = gatewayConfigGroup;
    }

    /**
     * 初始化configService
     * @return - ConfigServiceBean
     */
    @Bean
    public ConfigService initConfigService(){
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.SERVER_ADDR,NACOS_SERVER_ADDR);
        try {
            return NacosFactory.createConfigService(properties);
        } catch (NacosException e){
            log.error("【ConfigService】初始化失败");
            return null;
        }
    }
    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }


}
