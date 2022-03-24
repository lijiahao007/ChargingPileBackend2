package com.lijiahao.chargingpilebackend.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @GetMapping("/login")
    public Map<String, String> hello(@RequestParam("username") String userName, @RequestParam("password") String password) {
        return new HashMap<String, String>() {{
            put("username", userName);
            put("password", password);
        }};
    }

    @GetMapping("/login1")
    public String hello1(@RequestParam("username") String userName, @RequestParam("password") String password) {
        return "hello " + userName + " " + password;
    }

    @GetMapping("/login2")
    public User hello2(@RequestParam("username") String userName, @RequestParam("password") String password) {
        return new User(userName, password);
    }

    @GetMapping("/login4")
    public HashSet<User> hello3(@RequestParam("username") String userName, @RequestParam("password") String password) {
        return new HashSet<User>() {{
            add(new User(userName + "1", password + "1"));
            add(new User(userName + "2", password + "2"));
            add(new User(userName + "3", password + "3"));
        }};
    }

    @PostMapping("/login5")
    public User hello4(User user) {
        return user;
    }

    @PostMapping("/login6")
    public String hello5(@RequestParam(value = "picture") MultipartFile file, @RequestParam Map<String, Object> param) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        if (file.isEmpty()) {
            return objectMapper.writeValueAsString("文件为空");
        }

        try {
            byte[] bytes = file.getBytes();
            String fileName = file.getOriginalFilename();
            File img =  new File("C:\\Users\\10403\\Desktop\\imgs\\" + fileName);
            FileOutputStream fos = new FileOutputStream(img);
            fos.write(bytes);
            fos.close();
            return objectMapper.writeValueAsString(file.getName() + " " + file.getSize() + " " + file.getContentType() + " " + file.getOriginalFilename()); // 返回文件名
        } catch (IOException e) {
            e.printStackTrace();
            return objectMapper.writeValueAsString("IO错误");
        }

    }


    @GetMapping("/login7")
    public Resource hello6(@RequestParam("filePath") String filePath) {
        log.info("filePath : {}", filePath);
        File file = new File(filePath);
        return new FileSystemResource(file);
    }


    @GetMapping(value = "/login8", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    public ResponseEntity<Resource> hello7(@RequestParam("filePath") String filePath) {
        log.info("filePath : {}", filePath);
        File file = new File(filePath);
        FileSystemResource resource = new FileSystemResource(file);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Expires", "0");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .body(resource);
    }



}

