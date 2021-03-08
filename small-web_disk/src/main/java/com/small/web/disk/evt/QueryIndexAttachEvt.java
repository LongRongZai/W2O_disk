package com.small.web.disk.evt;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryIndexAttachEvt {

    //目录编码
    @ApiModelProperty(value = "目录编码", required = true)
    private String indexNo;

}
