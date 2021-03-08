package com.small.web.disk.evt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MoveAttachEvt {
    //目录编码
    @ApiModelProperty(value = "目录编码")
    private String indexNo;
    //附件编码
    @ApiModelProperty(value = "附件编码", required = true)
    private String attachNo;
}
