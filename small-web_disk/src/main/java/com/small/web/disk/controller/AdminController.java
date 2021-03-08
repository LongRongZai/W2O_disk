package com.small.web.disk.controller;

import com.small.web.disk.anotation.PassToken;
import com.small.web.disk.evt.AdminLoginEvt;
import com.small.web.disk.model.ServiceRespModel;
import com.small.web.disk.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Api(tags = "管理员相关接口")
public class AdminController {
    @Autowired//创建对象
    AdminService adminService;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 管理员登录
     */
    @PassToken
    @ApiOperation("管理员登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServiceRespModel login(@ModelAttribute AdminLoginEvt evt) {
        try {
            return adminService.login(evt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("管理员登录功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }
}
