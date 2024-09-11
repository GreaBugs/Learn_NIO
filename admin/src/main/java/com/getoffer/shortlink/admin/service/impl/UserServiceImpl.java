package com.getoffer.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.getoffer.shortlink.admin.common.convention.exception.ClientException;
import com.getoffer.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.getoffer.shortlink.admin.dao.entity.UserDO;
import com.getoffer.shortlink.admin.dao.mapper.UserMapper;
import com.getoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.getoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.getoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.getoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.getoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.getoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.getoffer.shortlink.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static com.getoffer.shortlink.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static com.getoffer.shortlink.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

/**
 * 用户接口实现层
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    // 注入布隆过滤器
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;

    // 分布式锁
    private final RedissonClient redissonClient;

    private final StringRedisTemplate stringRedisTemplate;

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
        return userRegisterCachePenetrationBloomFilter.contains(username);
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
        // 分布式锁       作为锁的前缀，加上用户名,确保每个用户的注册操作有一个独立的锁
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        try{
            if (lock.tryLock()){  // 尝试获取锁。如果当前没有其他线程在处理相同用户名的注册请求，锁会成功获取。如果锁无法获取，意味着另一个线程已经在处理相同的用户名注册，此时会抛出异常。
                // 2. 用户名不存在，插入新的用户数据
                int inserted = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                // 3. 检查插入结果，如果插入失败，抛出异常
                if (inserted < 1){
                    log.info("用户名插入失败");
                    throw new ClientException(USER_SAVE_ERROR);
                }
                log.info("成功插入用户名" + requestParam.getUsername());
                // 4. 使用布隆过滤器防止缓存穿透，新增已注册的用户名
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
                return;
            }
            throw new ClientException(USER_NAME_EXIST);
        }finally {
            lock.unlock();  // 保证了即使在代码发生异常的情况下，也会释放分布式锁，防止锁的永久持有。
        }
    }

    @Override
    public void update (@RequestBody UserUpdateReqDTO requestParam){
        // TODO 验证当前用户名是否为登录用户
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO login(@RequestBody UserLoginReqDTO requestParam){
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDel_flag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null){
            throw new ClientException("用户不存在");
        }
        Boolean hasLogin = stringRedisTemplate.hasKey("login_" + requestParam.getUsername());
        if (hasLogin != null && hasLogin){
            throw new ClientException("用户已登录, 请勿重复登陆！");
        }

        String uuid = UUID.randomUUID().toString();
//        stringRedisTemplate.opsForValue().set(uuid, JSON.toJSONString(requestParam), 30L, TimeUnit.MINUTES);
        stringRedisTemplate.opsForHash().put("login_" + requestParam.getUsername(), uuid, JSON.toJSONString(userDO));
        stringRedisTemplate.expire("login_" + requestParam.getUsername(), 30L, TimeUnit.MINUTES);
        return new UserLoginRespDTO(uuid);
    }

    @Override
    public boolean checkLogin(String username, String token){
        return stringRedisTemplate.opsForHash().get("login_" + username, token) != null;
    }

    @Override
    public void logout(String username, String token) {
        if (checkLogin(username, token)) {
            stringRedisTemplate.delete("login_" + username);
            return;
        }
        throw new ClientException("用户Token不存在或用户未登录");
    }
}
