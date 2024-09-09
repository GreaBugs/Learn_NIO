package com.getoffer.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getoffer.shortlink.admin.common.convention.exception.ClientException;
import com.getoffer.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.getoffer.shortlink.admin.dao.entity.UserDO;
import com.getoffer.shortlink.admin.dao.mapper.UserMapper;
import com.getoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.getoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.getoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static com.getoffer.shortlink.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static com.getoffer.shortlink.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

/**
 * 用户接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    @Override
    public UserRespDTO getUserByUsername(String username){
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null){
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        }

        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);  // source, target
        return result;
    }


    @Override
    public boolean hasUsername(String username){
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    /**
     * 用户注册
     * @param requestParam
     */
    @Override
    public void Register(UserRegisterReqDTO requestParam){
        // 1. 检查用户名是否已经存在
        if (hasUsername(requestParam.getUsername())){
            log.info("用户名已经存在");
            throw new ClientException(USER_NAME_EXIST);
        }
        // 2. 如果用户名不存在，插入新的用户数据
        int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
        // 3. 检查插入结果，如果插入失败，抛出异常
        if (inserted < 1){
            log.info("用户名插入失败");
            throw new ClientException(USER_SAVE_ERROR);
        }
        log.info("成功插入用户名" + requestParam.getUsername() + "已经存在");
        // 4. 使用布隆过滤器防止缓存穿透，新增已注册的用户名
        userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
    }

}
