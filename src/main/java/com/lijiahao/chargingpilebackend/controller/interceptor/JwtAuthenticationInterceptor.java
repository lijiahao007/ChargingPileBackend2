package com.lijiahao.chargingpilebackend.controller.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.controller.annotation.PassToken;
import com.lijiahao.chargingpilebackend.controller.exception.NoLoginException;
import com.lijiahao.chargingpilebackend.controller.exception.UserNotExistException;
import com.lijiahao.chargingpilebackend.entity.User;
import com.lijiahao.chargingpilebackend.service.impl.UserServiceImpl;
import com.lijiahao.chargingpilebackend.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Slf4j
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    UserServiceImpl userService;

    public JwtAuthenticationInterceptor(UserServiceImpl userService) {
        this.userService = userService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 预处理回调方法,实现处理器的预处理。第三个参数为响应的处理器,自定义Controller,返回值为true表示继续流程（如调用下一个拦截器或处理器）或者接着执行
        // 从请求头中取出 token  这里需要和前端约定好把jwt放到请求头一个叫token的地方
        String token = request.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        //检查是否有PassToken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        log.info("method->" + method.getName());
        log.info("是否存在PassToken注解" + method.isAnnotationPresent(PassToken.class));
        log.info("被JWT拦截验证了！！！！！！ token = " + token);
        if (token == null) {
            // 没有token, 未登录
            throw new NoLoginException();
        } else {
            String userId = JwtUtils.getUserID(token);
            log.info("userId:" + userId );
            log.info("userService:" + userService);
            long count = userService.count(new QueryWrapper<User>().eq("id", userId));
            if (count != 1) {
                // 没有该用户
                throw new UserNotExistException();
            }

            // 如果不通过验证会抛出异常TokenUnavailableException
            JwtUtils.verifyToken(token, userId);
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 后处理回调方法
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 整个请求处理完毕回调方法
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
