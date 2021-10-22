package top.zb.gatewayservice.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: polo
 * @Date: 2021/10/22 10:20
 */
@Slf4j
public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> List<T> toList(String jsonData, Class<T> beanType) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return mapper.readValue(jsonData, javaType);
        } catch (Exception e) {
            log.warn("", e);
        }
        return new ArrayList<T>();
    }

}
