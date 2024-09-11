package com.getoffer.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.getoffer.shortlink.admin.dao.entity.UserDO;
import com.getoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.getoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.getoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.getoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.getoffer.shortlink.admin.dto.resp.UserRespDTO;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserService extends IService<UserDO> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String username);


    boolean hasUsername(String username);

    void Register(UserRegisterReqDTO requestParam);

    /**
     * 根据用户名修改用户
     * @param requestParam：修改用户请求参数
     */
    void update (@RequestBody UserUpdateReqDTO requestParam);

    /**
     * 用户登录
     * @param requestParam 用户登录请求参数
     * @return
     */
    UserLoginRespDTO login(@RequestBody UserLoginReqDTO requestParam);

    /**
     * 检查用户是否登录
     * @param token
     * @return 返回一个boolean值，true代表已登录，false代表未登录
     */
    boolean checkLogin(String username, String token);


    void logout(String username, String token);

}