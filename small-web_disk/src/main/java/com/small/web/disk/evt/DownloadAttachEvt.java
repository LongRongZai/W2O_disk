package com.small.web.disk.evt;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
public class DownloadAttachEvt {

    //附件编码
    @ApiModelProperty(value = "附件编码", required = true)
    private String attachNo;
    //附件名称
    @ApiModelProperty(value = "附件名称", required = true)
    private String attachName;
    //用户编码
    @ApiModelProperty(value = "用户编码", required = true)
    private String userNo;

}
