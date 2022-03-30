package com.lijiahao.chargingpilebackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.annotation.PassToken;
import com.lijiahao.chargingpilebackend.controller.requestparam.*;
import com.lijiahao.chargingpilebackend.controller.response.LoginResponse;
import com.lijiahao.chargingpilebackend.controller.response.SignUpResponse;
import com.lijiahao.chargingpilebackend.controller.response.UserInfoResponse;
import com.lijiahao.chargingpilebackend.entity.User;
import com.lijiahao.chargingpilebackend.entity.UserExtend;
import com.lijiahao.chargingpilebackend.service.impl.UserExtendServiceImpl;
import com.lijiahao.chargingpilebackend.service.impl.UserServiceImpl;
import com.lijiahao.chargingpilebackend.utils.FileUtils;
import com.lijiahao.chargingpilebackend.utils.JwtUtils;
import com.lijiahao.chargingpilebackend.utils.NetworkUtils;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    UserExtendServiceImpl userExtendService;

    @ApiOperation("用户登录")
    @PassToken
    @PostMapping("/login")
    public LoginResponse login(@RequestBody AccountPwdRequest request) {
        log.warn("account:{},password:{}", request.getAccount(), request.getPassword());
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
        log.warn("account:{},password:{}", request.getAccount(), request.getPassword());
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
            user.setAvatarUrl("C:\\Users\\10403\\Desktop\\imgs\\user_pic\\default.png");
            user.setName("用户");
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

    @ApiOperation("获取用户信息")
    @GetMapping("/userInfo")
    public UserInfoResponse getUserInfo(@RequestParam String userId, HttpServletRequest request) {
        UserInfoResponse response = new UserInfoResponse();
        User user = userService.getById(userId);
        // 获取user信息
        List<UserExtend> extendList = userExtendService.list(new QueryWrapper<UserExtend>().eq("user_id", userId));
        Map<String, String> extendMap = new HashMap<>();

        // 获取user拓展信息
        extendList.forEach(userExtend -> {
           extendMap.put(userExtend.getField(), userExtend.getValue());
        });
        response.setExtend(extendMap);
        response.setUserId(userId);
        response.setName(user.getName());
        response.setPhone(user.getPhone());
        response.setRemark(user.getRemark());

        // 获取图片
        String localPath = user.getAvatarUrl();
        String url = "http://" + request.getServerName() + ":" + request.getServerPort() + "/user/userAvatar?url=" + localPath;
        response.setAvatarUrl(url);

        return response;
    }

    @ApiOperation("获取用户头像")
    @GetMapping("/userAvatar")
    public ResponseEntity<Resource> getUserAvatar(@RequestParam("url") String url) {
        // 根据url返回具体的图片
        File file = new File(url);
        FileSystemResource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "private");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(FileUtils.getImageFileType(url))
                .body(resource);
    }

    @ApiOperation("修改用户密码")
    @PostMapping("/modifyPwd")
    public String modifyPwd(@RequestBody ModifyPwdRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String userId = request.getUserId();
        User user = userService.getOne(new QueryWrapper<User>().eq("id", userId));
        if (userId.equals(user.getId().toString())) {
            // 修改密码
            user.setPassword(request.getNewPwd());
            userService.updateById(user);
            return mapper.writeValueAsString("success");
        } else {
            return mapper.writeValueAsString("failed");
        }
    }

    @ApiOperation("修改用户拓展信息")
    @PostMapping("/modifyExtendInfo")
    @Transactional
    public String modifyExtendInfo(@RequestBody ModifyExtendInfoRequest request) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuilder res = new StringBuilder();

        userExtendService.remove(new QueryWrapper<UserExtend>().eq("user_id", request.getUserId()));

        List<UserExtendInfo> list = request.getInfo();
        List<UserExtend> targetList = new ArrayList<>();

        for (UserExtendInfo info: list) {
            UserExtend tmp = new UserExtend();
            tmp.setField(info.getField());
            tmp.setValue(info.getValue());
            tmp.setUserId(Integer.valueOf(request.getUserId()));
            targetList.add(tmp);
        }
        userExtendService.saveBatch(targetList);

        return mapper.writeValueAsString("success");
    }

    @ApiOperation("修改用户信息（修改图像）")
    @PostMapping("/modifyUserInfo")
    @Transactional
    public String modifyUserInfo(@RequestParam("avatar") MultipartFile file,
                                 @RequestParam("request")ModifyUserInfoRequest request,
                                 HttpServletRequest httpServletRequest) throws IOException {
        // 返回图片url
        ObjectMapper mapper = new ObjectMapper();
        User user = userService.getOne(new QueryWrapper<User>().eq("id", request.getUserId()));
        File outputFile =  FileUtils.writeMultipartFileToLocal(file, "C:\\Users\\10403\\Desktop\\imgs\\user_pic", "user_pic_");
        user.setAvatarUrl(outputFile.getAbsolutePath());
        user.setRemark(request.getRemark());
        user.setName(request.getName());
        userService.updateById(user);
        String url = NetworkUtils.getRemoteUrl(httpServletRequest, "/user/userAvatar", outputFile.getAbsolutePath());
        return mapper.writeValueAsString(url);
    }

    @ApiOperation("修改用户信息（不修改头像）")
    @PostMapping("/modifyUserInfoWithoutPic")
    @Transactional
    public String modifyUserInfo(@RequestBody ModifyUserInfoRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = userService.getOne(new QueryWrapper<User>().eq("id", request.getUserId()));
        user.setId(request.getUserId());
        user.setRemark(request.getRemark());
        user.setName(request.getName());
        userService.updateById(user);
        return mapper.writeValueAsString("success");
    }

}

