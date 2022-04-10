package com.lijiahao.chargingpilebackend.base;

import com.lijiahao.chargingpilebackend.utils.QRCodeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class QRCodeTest {
    public static void main(String[] args) {
        try {
            File file = new File("C:\\Users\\10403\\Desktop\\qrcode.png");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            QRCodeUtils.getQRCode("这是一个二维码",fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
