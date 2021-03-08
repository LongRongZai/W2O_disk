package com.small.web.disk.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data//自动生成get set方法
@Entity//表明该类是一个实体
@Table(name = "t_admin")//表名
public class AdminBean {
    //管理员编码
    @Id//表示该字段为主键
    @Column(name = "adminNo", length = 32, nullable = false)//可设置字段名，类型，长度等
    private String adminNo;
    //管理员名称
    @Column(name = "adminName", length = 128, nullable = false)
    private String adminName;
    //管理员账号
    @Column(name = "adminAccount", length = 128, nullable = false)
    private String adminAccount;
    //管理员密码
    @Column(name = "adminPassword", length = 128, nullable = false)
    private String adminPassword;
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
