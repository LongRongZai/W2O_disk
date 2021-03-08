package com.small.web.disk.service;

import com.small.web.disk.bean.UserBean;
import com.small.web.disk.dao.mapperDao.AttachMapper;
import com.small.web.disk.dao.mapperDao.UserMapper;
import com.small.web.disk.evt.UserLoginEvt;
import com.small.web.disk.evt.UserRegisterEvt;
import com.small.web.disk.model.*;
import com.small.web.disk.utils.JwtUtils;
import com.small.web.disk.utils.Md5Util;
import com.small.web.disk.utils.UploadFileTool;
import com.small.web.disk.utils.VerificationCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class UserService {

    @Resource//创建对象（对接口运行效率更高）与@Autowired本质相同
    UserMapper userMapper;
    @Resource
    AttachMapper attachMapper;
    @Value("${mail.fromMail.sender}")
    String sender;// 发送者
    @Resource
    JavaMailSender javaMailSender;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 登录
     */
    public ServiceRespModel login(UserLoginEvt evt) {
        // 校验入参合法性
        if (StringUtils.isBlank(evt.getUserAccount()))//判断是否为null或""
            return new ServiceRespModel(-1, "账号不能为空", null);
        if (StringUtils.isBlank(evt.getUserPassword()))
            return new ServiceRespModel(-1, "密码不能为空", null);
        // 查询用户是否存在或被删除
        UserBean userBean = userMapper.queryUser(evt.getUserAccount());
        if (userBean == null)
            return new ServiceRespModel(-1, "用户不存在", null);
        // 验证密码是否正确
        if (!(Md5Util.MD5(evt.getUserPassword()).equals(userBean.getUserPassword())))
            return new ServiceRespModel(-1, "密码错误", null);
        //生成令牌
        LoginModel loginModel = new LoginModel();
        loginModel.setAccount(userBean.getUserAccount());
        loginModel.setName(userBean.getUserName());
        loginModel.setNo(userBean.getUserNo());
        String token = JwtUtils.createToken(loginModel);
        //更新用户最后一次登录时间
        int info = userMapper.updateUserLastLoginTime(evt.getUserAccount());
        if (info != 1) {
            logger.error(String.format("用户%s更新最后一次登录时间失败", evt.getUserAccount()));
        }
        //返回用户信息与令牌
        UserLoginModel userLoginModel = new UserLoginModel();
        userBean.setUserPassword(null);
        userLoginModel.setToken(token);
        userLoginModel.setUserBean(userBean);
        logger.info(String.format("用户%s登录成功", evt.getUserAccount()));
        return new ServiceRespModel(1, "登录成功", userLoginModel);
    }

    /**
     * 注册
     */
    public ServiceRespModel register(UserRegisterEvt evt) {
        // 检验入参合法性
        if (StringUtils.isBlank(evt.getUserName()))
            return new ServiceRespModel(-1, "昵称不能为空", null);
        if (StringUtils.isBlank(evt.getUserAccount()))
            return new ServiceRespModel(-1, "账号不能为空", null);
        if (StringUtils.isBlank(evt.getUserPassword()))
            return new ServiceRespModel(-1, "密码不能为空", null);
        if (StringUtils.isBlank(evt.getCode()))
            return new ServiceRespModel(-1, "验证码不能为空", null);
        if (StringUtils.isBlank(evt.getTime()))
            return new ServiceRespModel(-1, "验证码时效不能为空", null);
        if (StringUtils.isBlank(evt.getEncryptionCode()))
            return new ServiceRespModel(-1, "加密验证码不能为空", null);
        // 检验用户是否存在
        UserBean userBean = userMapper.queryUser(evt.getUserAccount());
        if (userBean != null)
            return new ServiceRespModel(-1, "用户已存在", null);
        //校验验证码
        if (!(Md5Util.MD5(evt.getCode() + evt.getUserAccount() + evt.getTime()).equals(evt.getEncryptionCode()))) {
            return new ServiceRespModel(-1, "验证码错误", null);
        }
        if (System.currentTimeMillis() - Long.parseLong(evt.getTime()) > 0) {
            return new ServiceRespModel(-1, "验证码已过期", null);
        }
        // 将用户数据保存至数据库
        UserBean addBean = new UserBean();
        addBean.setUserNo(StringUtils.replace(UUID.randomUUID().toString(), "-", ""));
        addBean.setUserName(evt.getUserName());
        addBean.setUserAccount(evt.getUserAccount());
        addBean.setUserPassword(Md5Util.MD5(evt.getUserPassword()));
        int info = userMapper.insertUser(addBean);
        if (info == 1) {
            logger.info(String.format("用户%s注册成功", evt.getUserAccount()));
            return new ServiceRespModel(1, "注册成功", null);
        }
        return new ServiceRespModel(-1, "注册失败", null);
    }

    /**
     * 查询用户信息
     */
    public ServiceRespModel queryUserInfo(HttpServletRequest request) {
        // 检验用户是否存在
        UserBean userInfo = userMapper.queryUserInfo((String) request.getAttribute("No"));
        if (userInfo == null)
            return new ServiceRespModel(-1, "用户不存在", null);
        //查询用户容量
        QueryAttachModel model = new QueryAttachModel();
        model.setUserNo((String) request.getAttribute("No"));
        UserInfoModel userInfoModel = new UserInfoModel();
        if (!attachMapper.queryAttachList(model).isEmpty()) {
            long totalSize = attachMapper.queryUserAttachSize((String) request.getAttribute("No"));
            userInfoModel.setCapacity(UploadFileTool.getPrintSize(totalSize));
        }
        userInfo.setUserPassword(null);
        userInfoModel.setUserBean(userInfo);
        // 返回用户信息
        return new ServiceRespModel(1, "用户信息", userInfoModel);
    }

    /**
     * 发送邮箱注册验证码
     */
    public ServiceRespModel sendEmail(String email) {
        // 检验用户是否存在
        UserBean userBean = userMapper.queryUser(email);
        if (userBean != null)
            return new ServiceRespModel(-1, "用户已存在", null);
        SimpleMailMessage message = new SimpleMailMessage();
        String code = VerificationCode.generateCode(4);   //随机数生成4位验证码
        message.setFrom(sender);
        message.setTo(email);
        message.setSubject("大大网盘");// 标题
        message.setText("【大大网盘】你的验证码为：" + code + " 有效期为5分钟");// 内容
        try {
            javaMailSender.send(message);
            VerificationCodeModel model = new VerificationCodeModel();
            String time = String.valueOf(System.currentTimeMillis() + 300000);
            String vCode = Md5Util.MD5(code + email + time);
            model.setCode(vCode);
            model.setTime(time);
            logger.info(String.format("注册邮件已发送至%s", email));
            return new ServiceRespModel(1, "邮件发送成功", model);
        } catch (MailSendException e) {
            return new ServiceRespModel(-1, "目标邮箱不存在", null);
        }
    }
}
