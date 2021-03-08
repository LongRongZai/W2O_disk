package com.small.web.disk.evt;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UploadAttachEvt {

    //目录编码
    @ApiModelProperty(value = "目录编码")
    private String indexNo;

}
