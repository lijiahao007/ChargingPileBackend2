package com.lijiahao.chargingpilebackend.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.MessageRequest;
import com.lijiahao.chargingpilebackend.entity.Message;
import com.lijiahao.chargingpilebackend.service.impl.MessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@ServerEndpoint("/publishMessage/{userId}")
public class WebSocketServer {

    /**静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。*/
    private static int onlineCount = 0;
    /**concurrent包的线程安全Set，用来存放每个客户端对应的WebSocket对象。*/
    private static final ConcurrentHashMap<String,WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**与某个客户端的连接会话，需要通过它来给客户端发送数据*/
    private Session session;
    /**接收userId*/
    private String userId = "";

    private ObjectMapper objectMapper = new ObjectMapper();

    private static MessageServiceImpl messageService;

    @Autowired
    public void setMessageService(MessageServiceImpl messageService) {
        WebSocketServer.messageService = messageService;
    }

    /**
     * 连接建立成
     * 功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) throws JsonProcessingException {
        this.session = session;
        this.userId=userId;
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //加入set中
            webSocketMap.put(userId,this);
        }else{
            //加入set中
            webSocketMap.put(userId,this);
            //在线数加1
            addOnlineCount();
        }
        log.warn("用户连接:"+userId+",当前在线人数为:" + getOnlineCount());

        // 发送当前用户未读取信息
        List<Message> unreadMessage = messageService.getNotReadMessageByUserId(userId);
        unreadMessage.forEach(message -> {
            if (message.getType().equals("IMAGE")) { // 图片类型的消息，需要一个 本地地址 -》 远程地址的过程
                String absolutePath = message.getText();
                String realPath = "/message/getChatImage?url=" + absolutePath;
                message.setText(realPath);
            }
           sendMessage(message.toMessageRequest());
           message.setState("READ");
        });

        // 保存状态
        messageService.updateBatchById(unreadMessage);

    }


    /**
     * 连接关闭
     * 调用的方法
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(userId)){
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.warn("用户退出:"+userId+",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消
     * 息后调用的方法
     * @param message
     * 客户端发送过来的消息
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        log.warn("用户消息:"+userId+",报文:"+message);
        sendMessage("server:" + message);
    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {

        log.error("用户错误:"+this.userId+",原因:"+error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务
     * 器主动推送
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(MessageRequest messageRequest) {
        try {
            String text = objectMapper.writeValueAsString(messageRequest);
            this.session.getBasicRemote().sendText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *发送自定
     *义消息
     **/
    public static void sendInfo(String message, String userId) {
        log.warn("发送消息到:"+userId+"，报文:"+message);
        if(StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)){
            webSocketMap.get(userId).sendMessage(message);
        }else{
            log.error("用户"+userId+",不在线！");
        }
    }

    /**
     * 获得此时的
     * 在线人数
     * @return
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线人
     * 数加1
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 在线人
     * 数减1
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    public static WebSocketServer getWebSocketServer(String userId) {
        return webSocketMap.get(userId);
    }

}
