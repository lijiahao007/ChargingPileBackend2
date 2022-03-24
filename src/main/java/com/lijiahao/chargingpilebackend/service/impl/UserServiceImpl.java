package com.lijiahao.chargingpilebackend.service.impl;

import com.lijiahao.chargingpilebackend.entity.User;
import com.lijiahao.chargingpilebackend.mapper.UserMapper;
import com.lijiahao.chargingpilebackend.service.IUserService;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
