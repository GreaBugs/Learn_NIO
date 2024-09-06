package com.getoffer.shortlink.admin.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户返回参数实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRespDTO {

    private Long id;
    private String username;
    private String realName;
    private String phone;
    private String mail;

}
