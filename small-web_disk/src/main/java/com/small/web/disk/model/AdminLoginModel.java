package com.small.web.disk.model;

import com.small.web.disk.bean.AdminBean;
import com.small.web.disk.bean.UserBean;
import lombok.Data;

@Data
public class AdminLoginModel {
    //令牌
    private String token;
    //管理员信息
    private AdminBean adminBean;
}
