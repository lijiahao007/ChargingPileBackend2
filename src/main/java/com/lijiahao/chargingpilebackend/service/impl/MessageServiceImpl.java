package com.lijiahao.chargingpilebackend.service.impl;

import com.lijiahao.chargingpilebackend.entity.Message;
import com.lijiahao.chargingpilebackend.mapper.MessageMapper;
import com.lijiahao.chargingpilebackend.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
