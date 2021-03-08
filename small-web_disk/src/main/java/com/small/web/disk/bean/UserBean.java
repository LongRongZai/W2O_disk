package com.small.web.disk.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data
@Entity
@Table(name = "t_user")
public class UserBean {
    //用户编码
    @Id
    @Column(name = "userNo", length = 32, nullable = false)
    private String userNo;
    //用户名称
    @Column(name = "userName", length = 128, nullable = false)
    private String userName;
    //用户账号
    @Column(name = "userAccount", length = 128, nullable = false)
    private String userAccount;
    //用户密码
    @Column(name = "userPassword", length = 128, nullable = false)
    private String userPassword;
    //用户容量
    @Column(name = "userCapacity", length = 128, nullable = false)
    private String userCapacity;
    //最近登录时间
    @Column(name = "lastLoginTime", length = 128)
    private Date lastLoginTime;
    //状态
    @Column(name = "status", length = 128, nullable = false)
    private String status;
    //创建时间
    @Column(name = "createTime", length = 128)
    private Date createTime;
    //创建人
    @Column(name = "createUser", length = 128)
    private String createUser;
    //更新时间
    @Column(name = "updateTime", length = 128)
    private Date updateTime;
    //更新人员
    @Column(name = "updateUser", length = 128)
    private String updateUser;
}
