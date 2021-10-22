package top.zb.gatewayservice.config;

import lombok.Data;


import java.util.List;

/**
 * 此类为将读取到nacos配置文件映射成当前bean
 * @Author: polo
 * @Date: 2021/10/22 17:27
 */
@Data
public class White {

    private List<String> whiteApiListNacos ;

    private List<String> whiteIpListNacos;

}
