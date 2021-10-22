package top.poloo.usercenter.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: polo
 * @Date: 2021/10/21 16:29
 */
@Data
@TableName("userBaseInfo")
public class UserBaseInfo implements Serializable {
    private static Long serializableId = 1L;
    private Long id;
    private String userName;
    private String password;
}
