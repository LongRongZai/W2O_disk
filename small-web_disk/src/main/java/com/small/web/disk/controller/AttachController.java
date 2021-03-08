package com.small.web.disk.controller;

import com.small.web.disk.anotation.PassToken;
import com.small.web.disk.evt.*;
import com.small.web.disk.service.AttachService;
import com.small.web.disk.model.ServiceRespModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/attach")
@Api(tags = "附件相关接口")
public class AttachController {
    @Autowired//创建对象
    AttachService attachService;
    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 查询目录附件列表
     */
    @ApiOperation("查询目录附件列表接口")
    @RequestMapping(value = "/queryIndexAttachList", method = RequestMethod.POST)
    public ServiceRespModel queryIndexAttachList(@ModelAttribute QueryIndexAttachEvt evt) {
        try {
            return attachService.indexAttachList(evt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询目录附件列表功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 附件上传
     */
    @ApiOperation("附件上传接口")
    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    //@ApiImplicitParam(name = "file", value = "附件", required = true)
    public ServiceRespModel upload(@RequestParam(value = "file") MultipartFile multipartFile, UploadAttachEvt evt, HttpServletRequest request) {
        try {
            return attachService.upload(multipartFile, evt, request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("附件上传功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 附件删除
     */
    @ApiOperation("附件删除接口")
    @RequestMapping(value = "/deleteAttach", method = RequestMethod.POST)
    public ServiceRespModel deleteAttach(@ModelAttribute DeleteAttachEvt evt, HttpServletRequest request) {
        try {
            return attachService.deleteAttach(evt, request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("附件删除功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 附件下载
     */
    @PassToken
    @ApiOperation("附件下载接口")
    @RequestMapping(value = "/downloadAttach", method = RequestMethod.GET)
    public void downloadAttach(HttpServletResponse response, DownloadAttachEvt evt) {
        try {
            attachService.downloadAttach(response, evt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("附件下载功能异常");
        }

    }

    /**
     * 附件名称修改
     */
    @ApiOperation("附件名称修改接口")
    @RequestMapping(value = "/updateAttach", method = RequestMethod.POST)
    public ServiceRespModel updateAttach(@ModelAttribute UpdateAttachNameEvt evt) {
        try {
            return attachService.updateAttach(evt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("附件修改功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 查询未审核附件列表
     */
    @ApiOperation("查询未审核附件列表接口")
    @RequestMapping(value = "/queryNonCheckedAttachList", method = RequestMethod.POST)
    public ServiceRespModel queryNonCheckedAttachList(HttpServletRequest request) {
        try {
            return attachService.NonCheckedAttachList(request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询未审核附件列表功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 附件审核
     */
    @ApiOperation("附件审核接口")
    @RequestMapping(value = "/auditAttach", method = RequestMethod.POST)
    public ServiceRespModel auditAttach(@ModelAttribute AuditAttachEvt evt,HttpServletRequest request) {
        try {
            return attachService.auditAttach(evt,request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("附件审核功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 附件移动
     */
    @ApiOperation("附件移动接口")
    @RequestMapping(value = "/moveAttach", method = RequestMethod.POST)
    public ServiceRespModel moveAttach(@ModelAttribute MoveAttachEvt evt) {
        try {
            return attachService.moveAttach(evt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("附件移动功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

}
