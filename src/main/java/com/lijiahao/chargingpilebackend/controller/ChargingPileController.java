package com.lijiahao.chargingpilebackend.controller;


import com.lijiahao.chargingpilebackend.utils.FileUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lijiahao
 * @since 2022-03-14
 */
@Controller
@RequestMapping("/chargingPile")
public class ChargingPileController {

    @ApiOperation("获取充电桩二维码")
    @GetMapping("getPileQrCodePic")
    public ResponseEntity<Resource> getPileQrCodePic(@RequestParam("url") String url) {
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

