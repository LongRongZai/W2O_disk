package com.small.web.disk.model;

import lombok.Data;

@Data
public class QueryAttachModel {
    //用户编码
    private String userNo;
    //目录编码
    private String indexNo;
    //审核状态
    private String auditStatus;

}
