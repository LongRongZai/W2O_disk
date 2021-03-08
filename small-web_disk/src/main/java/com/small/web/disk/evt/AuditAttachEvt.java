package com.small.web.disk.evt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AuditAttachEvt {
    //附件编码
    @ApiModelProperty(value = "附件编码", required = true)
    private String attachNo;
    //审核状态
    @ApiModelProperty(value = "审核状态(P = 通过，C = 审核中，D = 审核未通过)", required = true)
    private String auditStatus;
    //审核人
    @ApiModelProperty(value = "审核人", required = true)
    private String auditor;

}
