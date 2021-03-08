package com.small.web.disk.controller;

import com.small.web.disk.evt.AddIndexEvt;
import com.small.web.disk.evt.DeleteIndexEvt;
import com.small.web.disk.evt.UpdateIndexNameEvt;
import com.small.web.disk.service.AttachService;
import com.small.web.disk.service.IndexService;
import com.small.web.disk.model.ServiceRespModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/index")
@Api(tags = "目录相关接口")
public class IndexController {
    @Autowired//创建对象
    IndexService indexService;
    @Autowired//创建对象
    AttachService attachService;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 新增目录
     */
    @ApiOperation("新增目录接口")
    @RequestMapping(value = "/addIndex", method = RequestMethod.POST)
    public ServiceRespModel addIndex(@ModelAttribute AddIndexEvt evt, HttpServletRequest request) {
        try {
            return indexService.addIndex(evt, request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("新增目录功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 修改目录名称
     */
    @ApiOperation("修改目录名称接口")
    @RequestMapping(value = "/updateIndex", method = RequestMethod.POST)
    public ServiceRespModel updateIndex(@ModelAttribute UpdateIndexNameEvt evt) {
        try {
            return indexService.updateIndex(evt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("修改目录功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 删除目录
     */
    @ApiOperation("删除目录接口")
    @RequestMapping(value = "/deleteIndex", method = RequestMethod.POST)
    public ServiceRespModel deleteIndex(@ModelAttribute DeleteIndexEvt evt) {
        try {
            return indexService.deleteIndex(evt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除目录功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 查询用户目录列表&用户附件列表
     */
    @ApiOperation("查询用户目录列表和用户附件列表接口")
    @RequestMapping(value = "/queryUserAttachAndIndex", method = RequestMethod.POST)
    public ServiceRespModel queryUserAttachAndIndex(HttpServletRequest request) {
        try {
            return indexService.userAttachAndIndex(request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户目录列表&用户附件列表功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }
}
