package com.huawei.qrcode.qrcodedemo.controller.qrcode;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.huawei.qrcode.qrcodedemo.utils.QrcodeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;

/**
 * @author zhang
 * @since 2024-04-04 19:41
 */
@RestController
@RequestMapping("/qrcode")
public class QrcodeTestController {
    @GetMapping("/demo1")
    public void test() throws WriterException {
        HashMap configMap = new HashMap<>();
        // 设置二维码的误差校正级别，当二维码出现错误时，尝试修复数据
        configMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        // 设置二维码的字符集
        configMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 设置二维码的留白
        configMap.put(EncodeHintType.MARGIN, 5);

        MultiFormatWriter writer = new MultiFormatWriter();
        String content = "";

        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 300, 300, configMap);

        BufferedImage image = new BufferedImage(bitMatrix.getWidth(), bitMatrix.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < bitMatrix.getWidth(); i++) {
            for (int j = 0; j < bitMatrix.getHeight(); j++) {
                image.setRGB(i, j, bitMatrix.get(i, j) ? 0XFF000000 : 0xFFFFFFFF);
            }
        }
    }

    @GetMapping("/demo2")
    public String demo2() throws IOException {
        long startTime = System.currentTimeMillis();
        // 步骤1: 生成二维码
        BufferedImage qrCodeImage = generateQRCodeImage("Your QR Code Content Here");

//        // 步骤2: 读取给定图片
//        BufferedImage backgroundImage = ImageIO.read(new File("src/main/resources/static/background.png"));
//
//        // 步骤3: 将二维码合并到给定图片中央
//        BufferedImage combinedImage = mergeImages(backgroundImage, qrCodeImage, "sb吉豪");

        // 步骤4: 将合并后的图片转换为Base64字符串
        String base64Image = convertToBase64(qrCodeImage);

        System.out.println(System.currentTimeMillis() - startTime);

//        System.out.println(base64Image);

        return base64Image;
        // 步骤5: 保存到数据库
//        saveToDatabase(base64Image);
    }

    private BufferedImage generateQRCodeImage(String content) {
        QrConfig qrcodeConfig = QrcodeUtils.getQrcodeConfig(300, 300);
        BufferedImage generate = QrCodeUtil.generate("http://www.baidu.com", qrcodeConfig);
        return generate;
    }

    private BufferedImage mergeImages(BufferedImage background, BufferedImage qrCode, String text) {
        // 创建一个新的 BufferedImage，增加额外的高度用于文本
        int textHeight = 30; // 根据需要调整文本高度
        BufferedImage newImage = new BufferedImage(background.getWidth(), background.getHeight() + textHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newImage.createGraphics();

        // 绘制背景图片
        g.drawImage(background, 0, 0, null);

        // 计算二维码的位置并绘制
        int deltaX = (background.getWidth() - qrCode.getWidth()) / 2;
        int deltaY = (background.getHeight() - qrCode.getHeight()) / 2;
        g.drawImage(qrCode, deltaX, deltaY, null);

        // 设置文本属性（颜色、字体等）
        g.setColor(Color.BLACK); // 设置文本颜色
        g.setFont(new Font("Arial", Font.PLAIN, 20)); // 设置字体样式和大小

        // 计算文本的位置（这里我们放在图片下方中间）
        FontMetrics metrics = g.getFontMetrics();
        int x = (background.getWidth() - metrics.stringWidth(text)) / 2;
        int y = deltaY + qrCode.getHeight() + (textHeight - metrics.getHeight()) / 2 + metrics.getAscent();

        // 绘制文本
        g.drawString(text, x, y);

        // 释放资源
        g.dispose();

        return newImage;
    }

    private static String convertToBase64(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}
