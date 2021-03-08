package com.small.web.disk.evt;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AddIndexEvt {

    //目录名称
    @ApiModelProperty(value = "目录名称",required = true)
    private String indexName;

}
