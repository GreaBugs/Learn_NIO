package com.getoffer.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_user")
public class UserDO {

    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String mail;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime create_time;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime update_time;
    private Integer del_flag;
    private Long deletionTime;
}
