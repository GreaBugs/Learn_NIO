package com.getoffer.shortlink.admin.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("开始插入填充...");
        strictInsertFill(metaObject, "create_time", LocalDateTime::now, LocalDateTime.class);
        strictUpdateFill(metaObject, "update_time", LocalDateTime::now, LocalDateTime.class);
        strictUpdateFill(metaObject, "del_flag", () -> 0, Integer.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("开始更新填充...");
        strictUpdateFill(metaObject, "update_time", LocalDateTime::now, LocalDateTime.class);
    }


    public void del_flag(MetaObject metaObject) {
        log.info("开始更新填充...");
        strictUpdateFill(metaObject, "del_flag", ()->0, Integer.class);
    }
}
