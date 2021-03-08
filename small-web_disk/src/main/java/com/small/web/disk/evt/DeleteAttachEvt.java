package com.small.web.disk.evt;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DeleteAttachEvt {

    //附件编码
    @ApiModelProperty(value = "附件编码", required = true)
    private String attachNo;

}
