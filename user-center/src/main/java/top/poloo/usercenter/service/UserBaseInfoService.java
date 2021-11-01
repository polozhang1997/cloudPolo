package top.poloo.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.poloo.common.com.R;
import top.poloo.usercenter.entity.UserBaseInfo;

/**
 * @Author: polo
 * @Date: 2021/10/21 16:37
 */
public interface UserBaseInfoService extends IService<UserBaseInfo> {
    /**
     * 添加用户
     * @param userName - 用户名
     * @param password - 密码
     * @return
     */
    R<?> addUser(String userName, String password);

    /**
     * 验证用户名和密码
     * @param userName - 用户名
     * @param password - 密码
     * @return
     */
    R<?> checkUserNameAndPassword(String userName,String password);


}
