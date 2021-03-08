package com.small.web.disk.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "t_attach")
public class AttachBean {
    //附件编码
    @Id
    @Column(name = "attachNo", length = 32, nullable = false)
    private String attachNo;
    //用户编码
    @Column(name = "userNo", length = 32, nullable = false)
    private String userNo;
    //目录编码
    @Column(name = "indexNo", length = 32)
    private String indexNo;
    //附件名称
    @Column(name = "attachName", length = 128, nullable = false)
    private String attachName;
    //附件大小
    @Column(name = "attachSize", length = 128, nullable = false)
    private String attachSize;
    //附件大小(Byte)
    @Column(name = "attachByteSize", length = 128, nullable = false)
    private long attachByteSize;
    //附件类型
    @Column(name = "attachType", length = 128, nullable = false)
    private String attachType;
    //附件访问地址
    @Column(name = "attachUrl", length = 128, nullable = false)
    private String attachUrl;
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
    //审核状态（P 通过，C 审核中，D审核不通过）
    @Column(name = "auditStatus", length = 128, nullable = false)
    private String auditStatus;
    //审核人
    @Column(name = "auditor", length = 128)
    private String auditor;
    //审核时间
    @Column(name = "auditTime", length = 128)
    private Date auditTime;
    //附件预览地址
    @Column(name = "attachViewUrl", length = 128, nullable = false)
    private String attachViewUrl;

}
