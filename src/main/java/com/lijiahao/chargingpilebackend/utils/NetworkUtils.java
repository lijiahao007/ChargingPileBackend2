package com.lijiahao.chargingpilebackend.utils;

import javax.servlet.http.HttpServletRequest;

public class NetworkUtils {

    /**
     *
     * @param request HttpServletRequset请求
     * @param method 具体请求图片的方法 如 /user/userAvatar
     * @param localPath 图片本地连接
     * @return 远程Url
     */
    public static String getRemoteUrl(HttpServletRequest request, String method,String localPath) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + method +"?url=" + localPath;
    }
}
