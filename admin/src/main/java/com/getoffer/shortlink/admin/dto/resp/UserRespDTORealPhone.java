package com.getoffer.shortlink.admin.dto.resp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.getoffer.shortlink.admin.common.serialize.PhoneDesensitizationSerializer;
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

    @JsonSerialize(using = PhoneDesensitizationSerializer.class)
    private String phone;
    private String mail;

}
