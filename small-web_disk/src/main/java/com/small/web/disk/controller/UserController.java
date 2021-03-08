package com.small.web.disk.controller;

import com.small.web.disk.anotation.PassToken;
import com.small.web.disk.evt.UserLoginEvt;
import com.small.web.disk.evt.UserRegisterEvt;
import com.small.web.disk.service.UserService;
import com.small.web.disk.model.ServiceRespModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired//创建对象
    UserService userService;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 用户登录
     */
    @PassToken
    @ApiOperation("用户登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServiceRespModel login(@ModelAttribute UserLoginEvt evt) {
        try {
            return userService.login(evt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户登录功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 用户注册
     */
    @PassToken
    @ApiOperation("用户注册接口")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ServiceRespModel register(@ModelAttribute UserRegisterEvt evt) {
        try {
            return userService.register(evt);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("用户注册功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 查询用户详情
     */
    @ApiOperation("查询用户详情接口")
    @RequestMapping(value = "/userInfo", method = RequestMethod.POST)
    public ServiceRespModel queryUserInfo(HttpServletRequest request) {
        try {
            return userService.queryUserInfo(request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询用户详情功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }

    /**
     * 发送邮箱注册验证码
     */
    @PassToken
    @ApiOperation("发送邮箱注册验证码接口")
    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    @ApiImplicitParam(name = "email", value = "邮箱账号", required = true)
    public ServiceRespModel sendEmail(String email) {
        try {
            return userService.sendEmail(email);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("发送邮箱注册验证码功能异常");
            return new ServiceRespModel(-1, "系统异常", null);
        }
    }
}
