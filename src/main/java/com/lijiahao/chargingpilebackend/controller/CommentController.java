package com.lijiahao.chargingpilebackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.CommentRequest;
import com.lijiahao.chargingpilebackend.entity.ChargingPileStation;
import com.lijiahao.chargingpilebackend.entity.Comment;
import com.lijiahao.chargingpilebackend.service.impl.ChargingPileStationServiceImpl;
import com.lijiahao.chargingpilebackend.service.impl.CommentServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    CommentServiceImpl commentService;

    @Autowired
    ChargingPileStationServiceImpl chargingPileStationService;

    @ApiOperation("发表评论")
    @PostMapping("/publishComment")
    @Transactional
    public String publishComment(@RequestBody CommentRequest commentRequest) throws JsonProcessingException {
        Comment comment = new Comment();
        comment.setText(commentRequest.getText());
        comment.setStar(commentRequest.getStar());
        comment.setStationId(Integer.valueOf(commentRequest.getStationId()));
        comment.setUserId(Integer.valueOf(commentRequest.getUserId()));
        comment.setPileId(Integer.valueOf(commentRequest.getPileId()));
        comment.setLike(0);
        commentService.save(comment);

        ChargingPileStation station = chargingPileStationService.getById(comment.getStationId());
        int stationId = station.getId();
        List<Comment> list = commentService.list(new QueryWrapper<Comment>().eq("station_id", stationId));
        double sum = 0;
        if (list.size() != 0) {
            for (Comment c : list) {
                sum += Integer.parseInt(c.getStar());
            }
            sum /= list.size();
        }
        station.setScore(sum);
        chargingPileStationService.updateById(station);
        return mapper.writeValueAsString("success");
    }

    @ApiOperation("获取评论")
    @GetMapping("/queryCommentByStationId")
    public List<Comment> queryCommentByStationId(@RequestParam("stationId") String stationId) throws JsonProcessingException {
        return commentService.list(new QueryWrapper<Comment>().eq("station_id", stationId));
    }


    @ApiOperation("点赞评论")
    @GetMapping("/likeComment")
    public String likeComment(@RequestParam("commentId") String commentId, @RequestParam("userId") String userId) throws JsonProcessingException {
        // TODO("弄一张表 <点赞--点赞用户>映射"，在点赞之前判断是否能进行点赞)
        Comment comment = commentService.getById(commentId);
        comment.setLike(comment.getLike()+1);
        commentService.updateById(comment);
        return mapper.writeValueAsString(comment.getLike());
    }

    @ApiOperation("取消点赞评论")
    @GetMapping("/unlikeComment")
    public String unlikeComment(@RequestParam("commentId") String commentId, @RequestParam("userId") String userId) throws JsonProcessingException {
        // TODO("弄一张表 <点赞--点赞用户>映射"，在点赞之前判断是否能进行点赞)
        Comment comment = commentService.getById(commentId);
        comment.setLike(comment.getLike()-1);
        commentService.updateById(comment);
        return mapper.writeValueAsString(comment.getLike());
    }
}

