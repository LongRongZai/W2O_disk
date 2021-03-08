package com.small.web.disk.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
@Data
@Entity
@Table(name = "t_index")
public class IndexBean {
    //目录编码
    @Id
    @Column(name = "indexNo", length = 32, nullable = false)
    private String indexNo;
    //用户编码
    @Column(name = "userNo", length = 32, nullable = false)
    private String userNo;
    //目录名称
    @Column(name = "indexName", length = 128, nullable = false)
    private String indexName;
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
