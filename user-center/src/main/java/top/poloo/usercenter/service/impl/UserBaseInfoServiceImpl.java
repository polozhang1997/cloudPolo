package top.poloo.usercenter.service.impl;

import cn.hutool.core.util.HashUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.poloo.common.com.R;
import top.poloo.usercenter.entity.UserBaseInfo;
import top.poloo.usercenter.mapper.UserBaseInfoMapper;
import top.poloo.usercenter.service.UserBaseInfoService;

/**
 * @Author: polo
 * @Date: 2021/10/21 16:38
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class UserBaseInfoServiceImpl extends ServiceImpl<UserBaseInfoMapper, UserBaseInfo> implements UserBaseInfoService {
    @Override
    public R addUser(String userName, String password) {

        String s = String.valueOf(HashUtil.mixHash(password));
        UserBaseInfo userBaseInfo = this.baseMapper.selectOne(Wrappers.<UserBaseInfo>query().lambda().eq(UserBaseInfo::getUserName, userName));
        if (userBaseInfo != null){
            log.warn("添加已存在用户:{}",userName);
            return R.feignOperation(500,true,"用户已存在","");
        }
        int insert = this.baseMapper.insert(new UserBaseInfo().setUserName(userName).setPassword(s));
        return insert > 0 ? R.feignOperation(200,true,"保存成功","")
                          : R.feignOperation(500,false,"数据操作失败","");
    }

    @Override
    public R checkUserNameAndPassword(String userName, String password) {
        long hashPassword = HashUtil.mixHash(password);
        UserBaseInfo one = this.getOne(Wrappers.<UserBaseInfo>query().lambda().eq(UserBaseInfo::getUserName, userName).eq(UserBaseInfo::getPassword, password));

        return  one == null ? R.feignOperation(500,true,"用户不存在","")
                            : one.getPassword().equals(String.valueOf(hashPassword)) ?
                               R.feignOperation(200,true,"密码正确","")
                            : R.feignOperation(500,true,"密码不正确","");


    }
}
