package com.lijiahao.chargingpilebackend.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

@Slf4j
public class QRCodeUtils {
    // 该工具类参考 https://zhuanlan.zhihu.com/p/158576491
    /**
     * 默认宽度
     */
    private static final Integer WIDTH = 200;
    /**
     * 默认高度
     */
    private static final Integer HEIGHT = 200;

    /**
     * LOGO 默认宽度
     */
    private static final Integer LOGO_WIDTH = 22;
    /**
     * LOGO 默认高度
     */
    private static final Integer LOGO_HEIGHT = 22;

    /**
     * 图片格式
     */
    private static final String IMAGE_FORMAT = "png";
    private static final String CHARSET = "utf-8";

    /**
     * 生成二维码
     *
     * @param content    内容
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @return
     */
    private static BufferedImage crateQRCode(String content, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) {
        if (content != null && !content.equals("")) {
            ServletOutputStream stream = null;
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>(4);
            // 指定字符编码为utf-8
            hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
            // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            // 设置图片的边距
            hints.put(EncodeHintType.MARGIN, 2);
            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }
                if (logoUrl!= null && !logoUrl.equals("")) {
                    insertLogo(bufferedImage, width, height, logoUrl, logoWidth, logoHeight);
                }
                return bufferedImage;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (stream != null) {
                    try {
                        stream.flush();
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }


    /**
     * 二维码插入logo
     *
     * @param source     二维码
     * @param width      二维码宽度
     * @param height     二维码高度
     * @param logoUrl    logo 在线地址
     * @param logoWidth  logo 宽度
     * @param logoHeight logo 高度
     * @throws Exception
     */
    private static void insertLogo(BufferedImage source, Integer width, Integer height, String logoUrl, Integer logoWidth, Integer logoHeight) throws Exception {
        // logo 源可为 File/InputStream/URL
        Image src = ImageIO.read(new File(logoUrl));
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (width - logoWidth) / 2;
        int y = (height - logoHeight) / 2;
        graph.drawImage(src, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }


    /**
     * 获取二维码
     *
     * @param content 内容
     * @param output  输出流，二维码会输出到该流
     * @throws IOException
     */
    public static void getQRCode(String content, OutputStream output) throws IOException {
        BufferedImage image = crateQRCode(content, WIDTH, HEIGHT, null, null, null);
        ImageIO.write(image, IMAGE_FORMAT, output);
    }

    /**
     * 获取二维码
     *
     * @param content 内容
     * @param logoUrl logo资源
     * @param output  输出流
     * @throws Exception
     */
    public static void getQRCode(String content, String logoUrl, OutputStream output) throws Exception {
        BufferedImage image = crateQRCode(content, WIDTH, HEIGHT, logoUrl, LOGO_WIDTH, LOGO_HEIGHT);
        ImageIO.write(image, IMAGE_FORMAT, output);
    }

}
