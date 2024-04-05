package com.huawei.qrcode.qrcodedemo.utils;

import cn.hutool.extra.qrcode.QrConfig;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author zhang
 * @since 2024-04-05 16:00
 */
public class QrcodeUtils {

    public static QrConfig getQrcodeConfig(int width, int height) {
        QrConfig config = new QrConfig();
        config.setWidth(width);
        config.setHeight(height);
        config.setErrorCorrection(ErrorCorrectionLevel.M);
        return config;
    }


}
