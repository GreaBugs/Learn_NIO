package com.getoffer.shortlink.admin.test;

public class TestShardingSphere {

    public static final String SQL_TEMPLATE = "CREATE TABLE t_user_%d ( \n" +
            "  id BIGINT(20) AUTO_INCREMENT COMMENT 'ID', \n" +
            "  username VARCHAR(128) CHARACTER SET utf8mb4 NULL COMMENT '用户名', \n" +
            "  password VARCHAR(512) CHARACTER SET utf8mb4 NULL COMMENT '密码', \n" +
            "  real_name VARCHAR(256) CHARACTER SET utf8mb4 NULL COMMENT '真实姓名', \n" +
            "  phone VARCHAR(128) CHARACTER SET utf8mb4 NULL COMMENT '手机号', \n" +
            "  mail VARCHAR(512) CHARACTER SET utf8mb4 NULL COMMENT '邮件', \n" +
            "  deletion_time BIGINT(20) NULL COMMENT '注销时间戳', \n" +
            "  create_time DATETIME NULL COMMENT '创建时间', \n" +
            "  update_time DATETIME NULL COMMENT '修改时间', \n" +
            "  del_flag TINYINT(1) DEFAULT 0 COMMENT '删除标识 0: 未删除 1: 已删除', \n" +  // 设置 del_flag 默认值为 0
            "  PRIMARY KEY (id), \n" +
            "  UNIQUE KEY idx_unique_username (username) \n" +  // 省略 USING BTREE，因为 BTREE 是默认的
            ") COMMENT '用户表';";

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            String sql = String.format(SQL_TEMPLATE, i);
            System.out.println(sql);  // 输出生成的 SQL 语句到控制台，确保格式正确
        }
    }
}
