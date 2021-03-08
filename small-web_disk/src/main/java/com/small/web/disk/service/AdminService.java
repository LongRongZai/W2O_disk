package com.small.web.disk.service;

import com.small.web.disk.bean.AdminBean;
import com.small.web.disk.dao.mapperDao.AdminMapper;
import com.small.web.disk.evt.AdminLoginEvt;
import com.small.web.disk.model.AdminLoginModel;
import com.small.web.disk.model.LoginModel;
import com.small.web.disk.model.ServiceRespModel;
import com.small.web.disk.utils.JwtUtils;
import com.small.web.disk.utils.Md5Util;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminService {

    @Resource//创建对象（对接口运行效率更高）与@Autowired本质相同
    AdminMapper adminMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 管理员登录
     */
    public ServiceRespModel login(AdminLoginEvt evt) {
        // 校验入参合法性
        if (StringUtils.isBlank(evt.getAdminAccount()))//判断是否为null或""
            return new ServiceRespModel(-1, "账号不能为空", null);
        if (StringUtils.isBlank(evt.getAdminPassword()))
            return new ServiceRespModel(-1, "密码不能为空", null);
        // 查询管理员是否存在或被删除
        AdminBean adminBean = adminMapper.queryAdmin(evt.getAdminAccount());
        if (adminBean == null)
            return new ServiceRespModel(-1, "管理员不存在", null);
        // 验证密码是否正确
        if (!(Md5Util.MD5(evt.getAdminPassword()).equals(adminBean.getAdminPassword())))
            return new ServiceRespModel(-1, "密码错误", null);
        //生成令牌
        LoginModel loginModel = new LoginModel();
        loginModel.setNo(adminBean.getAdminNo());
        loginModel.setName(adminBean.getAdminName());
        loginModel.setAccount(adminBean.getAdminAccount());
        String token = JwtUtils.createToken(loginModel);
        //更新管理员最后一次登录时间
        int info = adminMapper.updateAdminLastLoginTime(evt.getAdminAccount());
        if (info != 1) {
            logger.error(String.format("管理员%s更新最后一次登录时间失败", evt.getAdminAccount()));
        }
        //返回管理员信息与令牌
        AdminLoginModel adminLoginModel = new AdminLoginModel();
        adminBean.setAdminPassword(null);
        adminLoginModel.setToken(token);
        adminLoginModel.setAdminBean(adminBean);
        logger.info(String.format("管理员%s登录成功", evt.getAdminAccount()));
        return new ServiceRespModel(1, "登录成功", adminLoginModel);
    }

}
