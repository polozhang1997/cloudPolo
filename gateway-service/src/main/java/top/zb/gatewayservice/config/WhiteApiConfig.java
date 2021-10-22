package top.zb.gatewayservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: polo
 * @Date: 2021/10/22 15:51
 */
@Configuration
public class WhiteApiConfig {

    public static List<String> whiteApiList = new ArrayList<>();

    public static List<String> whiteIpList = new ArrayList<>();


    public static String DATA_ID;

    public static String GROUP;

    @Value("${polo.nacos.white.dataId}")
    public void setWhiteApi(String dataId){
        DATA_ID = dataId;
    }
    @Value("${polo.nacos.white.group}")
    public void setGroup(String group) {
        GROUP = group;
    }





}
