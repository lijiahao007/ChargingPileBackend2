package com.lijiahao.chargingpilebackend.service.impl;

import com.lijiahao.chargingpilebackend.entity.Order;
import com.lijiahao.chargingpilebackend.mapper.OrderMapper;
import com.lijiahao.chargingpilebackend.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

}
