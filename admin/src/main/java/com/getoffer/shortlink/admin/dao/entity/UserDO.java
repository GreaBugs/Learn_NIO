package com.getoffer.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.getoffer.shortlink.admin.common.database.BaseDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class UserDO extends BaseDO {

    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String mail;
    private Long deletionTime;
}
