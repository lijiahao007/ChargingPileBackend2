package com.lijiahao.chargingpilebackend.utils;


import org.springframework.http.MediaType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
    public static MediaType getImageFileType(String fileName) {
        if (fileName.endsWith(".gif")){
            return MediaType.IMAGE_GIF;
        } else if (fileName.endsWith(".png")){
            return MediaType.IMAGE_PNG;
        } else if (fileName.endsWith("jpg") || fileName.endsWith("jpeg")){
            return MediaType.IMAGE_JPEG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    public static String getFileSuffix(String fileName) {
        // 获取后缀
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static void writeByteArrayToFile(File file, byte[] bytes) throws IOException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }
}
