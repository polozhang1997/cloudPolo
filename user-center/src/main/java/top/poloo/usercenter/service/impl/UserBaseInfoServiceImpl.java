package top.poloo.usercenter.service.impl;

import cn.hutool.core.util.HashUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.poloo.common.com.AuthUser;
import top.poloo.common.com.R;
import top.poloo.common.constant.TokenConstant;
import top.poloo.common.util.JwtUtils;
import top.poloo.usercenter.entity.UserBaseInfo;
import top.poloo.usercenter.mapper.UserBaseInfoMapper;
import top.poloo.usercenter.service.UserBaseInfoService;
import java.util.HashMap;
import java.util.Map;

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
            return R.failed("用户已存在");
        }
        int insert = this.baseMapper.insert(new UserBaseInfo().setUserName(userName).setPassword(s));
        return insert > 0 ? R.success("添加成功")
                          : R.failed("数据操作失败");
    }

    @Override
    public R checkUserNameAndPassword(String userName, String password) {
        UserBaseInfo one = this.getOne(Wrappers.<UserBaseInfo>query().lambda().eq(UserBaseInfo::getUserName, userName));
        //  long hashPassword = HashUtil.mixHash(password);
        if (one == null){
            return R.failed("账户不存在");
        }
        //生成token
        Map<String,Object> claims  = new HashMap<>(16);
        claims.put("userName",one.getUserName());
        claims.put("neverMind","poloo");
        String token = JwtUtils.generateToken(claims, one.getUserName(), TokenConstant.OUT_TIME);
        if (one.getPassword().equals(String.valueOf(password))){
            AuthUser authUser = new AuthUser().setUserId(one.getId().toString()).setNickName(one.getUserName()).setJwtToken(token)
                    .setOutTime(TokenConstant.OUT_TIME);
            return R.success(authUser);
        }else {
            return R.failed("密码不正确");
        }
    }

}
