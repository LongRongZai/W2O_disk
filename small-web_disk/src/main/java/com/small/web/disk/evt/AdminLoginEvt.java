package com.small.web.disk.evt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AdminLoginEvt {
    //管理员账号
    @ApiModelProperty(value = "管理员账号",required = true)
    private String adminAccount;
    //管理员密码
    @ApiModelProperty(value = "管理员密码",required = true)
    private String adminPassword;

}

