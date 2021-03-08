package com.small.web.disk.model;

import com.small.web.disk.bean.AttachBean;
import com.small.web.disk.bean.IndexBean;
import lombok.Data;

import java.util.List;
@Data
public class AttachIndexModel {
    //目录列表
    private List<IndexBean> indexBeanList;
    //附件列表
    private List<AttachBean> attachBeanList;

}
