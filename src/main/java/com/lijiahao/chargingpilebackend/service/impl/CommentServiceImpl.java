package com.lijiahao.chargingpilebackend.service.impl;

import com.lijiahao.chargingpilebackend.service.ICommentService;
import com.lijiahao.chargingpilebackend.entity.Comment;
import com.lijiahao.chargingpilebackend.mapper.CommentMapper;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

}
