package com.lijiahao.chargingpilebackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.MessageRequest;
import com.lijiahao.chargingpilebackend.entity.Message;
import com.lijiahao.chargingpilebackend.service.impl.MessageServiceImpl;
import com.lijiahao.chargingpilebackend.utils.FileUtils;
import com.lijiahao.chargingpilebackend.utils.TimeUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-24
 */
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Autowired
    MessageServiceImpl messageService;

    @ApiOperation("根据userId的消息推送")
    @GetMapping("/getMessageById")
    public List<Message> getMessageById(@RequestParam("userId") String userId) {
        int id = Integer.parseInt(userId);
        return messageService.list(new QueryWrapper<Message>()
                .eq("send_user_id", id)
                .or()
                .eq("target_user_id", id));
    }

    @ApiOperation("发送文本消息")
    @PostMapping("/sendTextMessage")
    public String sendTextMessage(@RequestBody MessageRequest request) throws JsonProcessingException {
        LocalDateTime time = TimeUtils.longToLocalDateTime(request.getTimeStamp());

        String state = "NOT_READ";
        // 查看是否该用户是否在线，在则推送消息给他
        WebSocketServer server =  WebSocketServer.getWebSocketServer(request.getTargetUserId());
        if (server != null) {
            state = "READ";
            server.sendMessage(request);
        }

        // 将消息存储在数据库中
        Message message = new Message(
                request.getUuid(),
                time,
                request.getType(),
                Integer.valueOf(request.getSendUserId()),
                Integer.valueOf(request.getTargetUserId()),
                request.getText(),
                state
        );
        log.warn("message!!!!!" + message.toString());
        messageService.save(message);


        return new ObjectMapper().writeValueAsString("success");
    }

    // 返回远程图片
    @ApiOperation("发送图片消息")
    @PostMapping("/sendImageMessage")
    public String sendImageMessage(@RequestParam("pic") MultipartFile picture, @RequestParam("messageRequest") MessageRequest request, HttpServletRequest httpServletRequest) throws IOException {
        File file = FileUtils.writeMultipartFileToLocal(picture, "C:\\Users\\10403\\Desktop\\imgs\\chat_pic\\", "chat_pic");

        String path = "/message/getChatImage?url=" + file.getAbsolutePath(); // 具体的服务器地址与端口号在客户端自行配置
        String state = "NOT_READ";

        // 查看目标用户是否在线，如果在则将消息发送给他
        WebSocketServer server = WebSocketServer.getWebSocketServer(request.getTargetUserId());
        if (server != null) {
            state = "READ";
            request.setText(path); // 将text修改成remoteUrl后，发送给目标用户。
            server.sendMessage(request);
        }

        // 将消息存储在数据库中
        Message message = new Message(
                request.getUuid(),
                TimeUtils.longToLocalDateTime(request.getTimeStamp()),
                "IMAGE",
                Integer.valueOf(request.getSendUserId()),
                Integer.valueOf(request.getTargetUserId()),
                file.getPath(),
                state);
        messageService.save(message);

        return new ObjectMapper().writeValueAsString(path);
    }

    @ApiOperation("获取聊天图片")
    @GetMapping("/getChatImage")
    public ResponseEntity<Resource> getChatPic(@RequestParam("url") String url) {
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

}
