package com.small.web.disk.utils;

import com.small.web.disk.anotation.PassToken;
import com.small.web.disk.bean.AdminBean;
import com.small.web.disk.bean.UserBean;
import com.small.web.disk.dao.mapperDao.AdminMapper;
import com.small.web.disk.dao.mapperDao.UserMapper;
import com.small.web.disk.exceptions.TokenUnavailableException;
import com.small.web.disk.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Lehr
 * @create: 2020-02-03
 */
public class JwtAuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    UserMapper userMapper;
    @Resource
    AdminMapper adminMapper;

    Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 从请求头中取出 token  这里需要和前端约定好把jwt放到请求头一个叫Authorization的地方
        String token = httpServletRequest.getHeader("Authorization");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //默认全部检查
        else {
            // 执行认证
            if (token == null) {
                logger.error("需要令牌进行验证");
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            // 获取 token 中的 No
            String check = null;
            try {
                check = JwtUtils.getAudience(token);
            } catch (TokenUnavailableException e) {
                logger.error(e.getMessage());
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            //验证管理员或用户是否存在
            UserBean user = userMapper.queryUserInfo(check);
            AdminBean admin = adminMapper.queryAdminInfo(check);
            if (user == null && admin == null) {
                logger.error("此用户或管理员不存在");
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            // 验证 token
            try {
                JwtUtils.verifyToken(token, check);
            } catch (TokenUnavailableException e) {
                logger.error(e.getMessage());
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }

            //获取载荷内容
            String name = JwtUtils.getClaimByName(token, "Name").asString();
            String account = JwtUtils.getClaimByName(token, "Account").asString();
            String no = JwtUtils.getClaimByName(token, "No").asString();


            //放入attribute以便后面调用
            httpServletRequest.setAttribute("Name", name);
            httpServletRequest.setAttribute("Account", account);
            httpServletRequest.setAttribute("No", no);


            return true;

        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }
}
