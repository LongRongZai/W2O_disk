package com.small.web.disk.evt;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateAttachNameEvt {

    //附件编码
    @ApiModelProperty(value = "附件编码", required = true)
    private String attachNo;
    //附件名称
    @ApiModelProperty(value = "附件名称", required = true)
    private String attachName;

}
