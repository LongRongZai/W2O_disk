package com.small.web.disk.model;

import com.small.web.disk.bean.UserBean;
import lombok.Data;

@Data
public class UserInfoModel {
    //用户信息
    private UserBean userBean;
    //用户容量
    private String capacity = "O.00   ";
}
