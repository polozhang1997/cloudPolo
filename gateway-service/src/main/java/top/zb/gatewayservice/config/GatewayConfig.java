package top.zb.gatewayservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: polo
 * @Date: 2021/10/21 17:06
 */
@Configuration
public class GatewayConfig {
    public static final long DEFAULT_TIMEOUT = 30000;


    public static String GATEWAY_CONFIG_DATA_ID;
    @Value("${polo.gateway.group}")
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




}
