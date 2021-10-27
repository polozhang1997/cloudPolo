package top.poloo.usercenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: polo
 * @Date: 2021/10/21 16:29
 */
@Data
@TableName("userBaseInfo")
@Accessors(chain = true)
public class UserBaseInfo implements Serializable {
    private static Long serializableId = 1L;

    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    private Long id;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "password")
    private String password;
}
