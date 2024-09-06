package com.getoffer.shortlink.admin.common.convention.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Result<T> implements Serializable {

    public static final String SUCCESS_CODE = "0";
    private String code;
    private String message;
    private T data;
    private String requestId;
    public boolean isSuccess(){
        return SUCCESS_CODE.equals(code);
    }

}
