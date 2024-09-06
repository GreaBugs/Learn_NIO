package com.getoffer.shortlink.admin.controller;

import com.getoffer.shortlink.admin.common.convention.result.Result;
import com.getoffer.shortlink.admin.common.convention.result.Results;
import com.getoffer.shortlink.admin.common.enums.UserErrorCodeEnum;
import com.getoffer.shortlink.admin.dto.resp.UserRespDTO;
import com.getoffer.shortlink.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制层
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shortlink/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username){
        UserRespDTO result =  userService.getUserByUsername(username);
        if (result == null){
            return new Result<UserRespDTO>().setCode(UserErrorCodeEnum.USER_NULL.code())
                            .setMessage(UserErrorCodeEnum.USER_NULL.message());
        } else{
            return Results.success(result);
        }
    }
}
