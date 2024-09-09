package com.getoffer.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import com.getoffer.shortlink.admin.common.convention.result.Result;
import com.getoffer.shortlink.admin.common.convention.result.Results;
import com.getoffer.shortlink.admin.dto.req.UserRegisterReqDTO;
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

}
