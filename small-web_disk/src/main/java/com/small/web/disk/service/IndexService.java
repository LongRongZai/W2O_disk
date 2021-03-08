package com.small.web.disk.service;

import com.small.web.disk.bean.AttachBean;
import com.small.web.disk.bean.IndexBean;
import com.small.web.disk.dao.mapperDao.AttachMapper;
import com.small.web.disk.dao.mapperDao.IndexMapper;
import com.small.web.disk.evt.AddIndexEvt;
import com.small.web.disk.evt.DeleteIndexEvt;
import com.small.web.disk.evt.UpdateIndexNameEvt;
import com.small.web.disk.model.AttachIndexModel;
import com.small.web.disk.model.QueryAttachModel;
import com.small.web.disk.model.ServiceRespModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Service
public class IndexService {
    @Resource
    IndexMapper indexMapper;
    @Resource
    AttachMapper attachMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 新增目录
     */
    public ServiceRespModel addIndex(AddIndexEvt evt, HttpServletRequest request) {
        //检验入参合法性
        if (StringUtils.isBlank(evt.getIndexName()))
            return new ServiceRespModel(-1, "目录名称不能为空", null);
        //新增目录
        IndexBean addBean = new IndexBean();
        addBean.setIndexName(evt.getIndexName());
        addBean.setIndexNo(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
        addBean.setUserNo((String) request.getAttribute("No"));
        int info = indexMapper.insertIndex(addBean);
        if (info == 1)
            return new ServiceRespModel(1, "新增目录成功", null);
        return new ServiceRespModel(-1, "新增目录失败", null);
    }

    /**
     * 修改目录名称
     */
    public ServiceRespModel updateIndex(UpdateIndexNameEvt evt) {
        //检验入参合法性
        if (StringUtils.isBlank(evt.getIndexName()))
            return new ServiceRespModel(-1, "目录名称不能为空", null);
        if (StringUtils.isBlank(evt.getIndexNo()))
            return new ServiceRespModel(-1, "目录编码不能为空", null);
        //检验目录是否存在
        IndexBean indexBean = indexMapper.queryIndex(evt.getIndexNo());
        if (indexBean == null)
            return new ServiceRespModel(-1, "目录不存在", null);
        //修改目录
        IndexBean updateBean = new IndexBean();
        updateBean.setIndexNo(evt.getIndexNo());
        updateBean.setIndexName(evt.getIndexName());
        int info = indexMapper.updateIndex(updateBean);
        if (info == 1)
            return new ServiceRespModel(1, "修改目录名称成功", null);
        return new ServiceRespModel(-1, "修改目录名称失败", null);
    }

    /**
     * 删除目录
     */
    public ServiceRespModel deleteIndex(DeleteIndexEvt evt) {
        //检验入参合法性
        if (StringUtils.isBlank(evt.getIndexNo()))
            return new ServiceRespModel(-1, "目录编码不能为空", null);
        //检验目录是否存在
        IndexBean indexBean = indexMapper.queryIndex(evt.getIndexNo());
        if (indexBean == null)
            return new ServiceRespModel(-1, "目录不存在", null);
        //将目录下的附件移出该目录
        attachMapper.moveAttachWhenDelete(evt.getIndexNo());
        //删除目录
        int info = indexMapper.deleteIndex(evt.getIndexNo());
        if (info == 1)
            return new ServiceRespModel(1, "删除目录成功", null);
        return new ServiceRespModel(-1, "删除目录失败", null);
    }

    /**
     * 用户目录列表&用户附件列表
     */
    public ServiceRespModel userAttachAndIndex(HttpServletRequest request) {
        //查询用户目录列表
        List<IndexBean> indexBeanList = indexMapper.userIndexList((String) request.getAttribute("No"));
        //查询用户附件列表
        QueryAttachModel model = new QueryAttachModel();
        model.setUserNo((String) request.getAttribute("No"));
        List<AttachBean> attachBeanList = attachMapper.queryAttachList(model);
        //返回用户目录列表&用户附件列表
        AttachIndexModel attachIndexModel = new AttachIndexModel();
        attachIndexModel.setIndexBeanList(indexBeanList);
        attachIndexModel.setAttachBeanList(attachBeanList);
        return new ServiceRespModel(1, "用户目录列表&用户附件列表", attachIndexModel);
    }
}
