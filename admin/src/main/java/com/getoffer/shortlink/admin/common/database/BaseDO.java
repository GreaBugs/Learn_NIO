package com.getoffer.shortlink.admin.common.database;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDO {
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime create_time;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime update_time;
    private Integer del_flag;
}
