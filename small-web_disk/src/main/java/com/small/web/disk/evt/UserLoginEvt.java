package com.small.web.disk.evt;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserLoginEvt {

    //用户账号
    @ApiModelProperty(value = "用户账号", required = true)
    private String userAccount;
    //用户密码
    @ApiModelProperty(value = "用户密码", required = true)
    private String userPassword;

}
