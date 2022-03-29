package com.lijiahao.chargingpilebackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.annotation.PassToken;
import com.lijiahao.chargingpilebackend.controller.requestparam.AccountPwdRequest;
import com.lijiahao.chargingpilebackend.controller.response.LoginResponse;
import com.lijiahao.chargingpilebackend.controller.response.SignUpResponse;
import com.lijiahao.chargingpilebackend.entity.User;
import com.lijiahao.chargingpilebackend.service.impl.UserServiceImpl;
import com.lijiahao.chargingpilebackend.utils.JwtUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @ApiOperation("用户登录")
    @PassToken
    @PostMapping("/login")
    public LoginResponse login(@RequestBody AccountPwdRequest request) {
        log.info("account:{},password:{}", request.getAccount(), request.getPassword());
        LoginResponse response = new LoginResponse();

        // 1. 根据账号获取用户密码
        long count = userService.count(new QueryWrapper<User>().eq("phone", request.getAccount()));
        if (count == 1) {
            // 2. 如果存在该用户则判断密码是否正确
            User user = userService.getOne(new QueryWrapper<User>().eq("phone", request.getAccount()));
            if (user.getPassword().equals(request.getPassword())) {
                // 3. 密码正确，生成token。
                String token = JwtUtils.createToken(user.getId().toString());
                response.setCode("success");
                response.setUserId(user.getId().toString());
                response.setToken(token);
            } else {
                response.setCode("wrong pwd");
            }
        } else {
            response.setCode("not exists");
        }

        // code :
        //  success -> 登录成功；
        //  wrong pwd -> 密码错误；
        //  not exists -> 账号不存在；
        return response;
    }

    @ApiOperation("用户注册")
    @PassToken
    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody AccountPwdRequest request) {
        log.info("account:{},password:{}", request.getAccount(), request.getPassword());
        SignUpResponse response = new SignUpResponse();

        // 1. 检查是否已有该用户
        long count = userService.count(new QueryWrapper<User>().eq("phone", request.getAccount()));
        if (count > 0) {
            response.setCode("exists");
        } else {
            // 2. 插入用户
            User user = new User();
            user.setPhone(request.getAccount());
            user.setPassword(request.getPassword());
            boolean flag = userService.save(user);
            if (flag) {
                response.setCode("success");
                response.setUserId("1");
            } else {
                response.setCode("failed");
            }
        }
        return response;
    }

}

