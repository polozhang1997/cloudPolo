package top.poloo.usercenter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.poloo.usercenter.entity.UserBaseInfo;
import top.poloo.usercenter.mapper.UserBaseInfoMapper;
import top.poloo.usercenter.service.UserBaseInfoService;

/**
 * @Author: polo
 * @Date: 2021/10/21 16:38
 */
@Service
public class UserBaseInfoServiceImpl extends ServiceImpl<UserBaseInfoMapper, UserBaseInfo> implements UserBaseInfoService {
}
