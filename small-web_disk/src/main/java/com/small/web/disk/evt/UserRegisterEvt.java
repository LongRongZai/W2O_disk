package com.small.web.disk.evt;


import com.small.web.disk.model.VerificationCodeModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@Data
public class UserRegisterEvt {

    //用户名称
    @ApiModelProperty(value = "用户名称", required = true)
    private String userName;
    //用户账号
    @ApiModelProperty(value = "用户账号", required = true)
    private String userAccount;
    //用户密码
    @ApiModelProperty(value = "用户密码", required = true)
    private String userPassword;
    //验证码
    @ApiModelProperty(value = "验证码", required = true)
    private String code;
    //验证码时效
    @ApiModelProperty(value = "验证码时效", required = true)
    private String time;
    //加密验证码
    @ApiModelProperty(value = "加密验证码", required = true)
    private String encryptionCode;

}
