package com.lijiahao.chargingpilebackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lijiahao.chargingpilebackend.entity.Message;
import com.lijiahao.chargingpilebackend.mapper.MessageMapper;
import com.lijiahao.chargingpilebackend.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-24
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    public List<Message> getNotReadMessageByUserId(String userId) {
        return list(new QueryWrapper<Message>()
                .eq("target_user_id", userId)
                .eq("state", "NOT_READ")
        );
    }
}
