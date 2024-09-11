package com.getoffer.shortlink.admin.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateReqDTO {
    private Long id;
    private String username;
    private String realName;
    private String password;
    private String phone;
    private String mail;
}
