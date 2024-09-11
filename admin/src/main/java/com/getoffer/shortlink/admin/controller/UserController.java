package com.getoffer.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.getoffer.shortlink.admin.common.convention.result.Result;
import com.getoffer.shortlink.admin.common.convention.result.Results;
import com.getoffer.shortlink.admin.dto.req.UserLoginReqDTO;
import com.getoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.getoffer.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.getoffer.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.getoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.getoffer.shortlink.admin.dto.resp.UserRespDTORealPhone;
import com.getoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/short-link/v1/user")
public class UserController {

    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username){
            return Results.success(userService.getUserByUsername(username));
    }

    /**
     * 根据用户名查询用户信息，无脱敏用户信息
     * @param username
     * @return
     */
    @GetMapping("/real/{username}")
    public Result<UserRespDTORealPhone> getUserByUsernameRealPhone(@PathVariable("username") String username){
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UserRespDTORealPhone.class));
    }

    @GetMapping("/hasUsername/{username}")
    public Result<UserRespDTO> hasUsername(@PathVariable("username") String username){
        if (userService.hasUsername(username)){
            return Results.success(userService.getUserByUsername(username)).setMessage("用户名已存在");
        }else{
            return Results.success(userService.getUserByUsername(username)).setMessage("用户名不存在");
        }
    }


    @PostMapping()
    public Result<Void> register (@RequestBody UserRegisterReqDTO requestParam){
        userService.Register(requestParam);
        return Results.success();
    }

    @PutMapping()
    public Result<Void> update (@RequestBody UserUpdateReqDTO requestParam){
        userService.update(requestParam);
        return Results.success();
    }

    @PostMapping("/login")
    public Result<UserLoginRespDTO> login (@RequestBody UserLoginReqDTO requestParam){
        UserLoginRespDTO userLoginRespDTO = userService.login(requestParam);
        return Results.success(userLoginRespDTO);
    }

    @PostMapping("/check-login")
    public Result<String> checkLogin (@RequestParam("username") String username, @RequestParam("token") String token){
        boolean login_flag = userService.checkLogin(username, token);
        if (login_flag){
            return Results.success("用户已登录");
        }else {
            return Results.success("用户未登录");
        }
    }

    /**
     * 用户退出登录
     */
    @DeleteMapping("/logout")
    public Result<Void> logout(@RequestParam("username") String username, @RequestParam("token") String token) {
        userService.logout(username, token);
        return Results.success();
    }

}
