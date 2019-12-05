package com.esimtek.gemalto.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 *
 * @author wang
 */
public class EncryptionUtil {

    public static final String SECRET_KEY = "esim58182600";

    /**
     * 利用java原生的摘要实现SHA256加密
     *
     * @param unEncrypt 待加密字符
     * @param type      加密类型
     * @return 加密后字符
     */
    public static String encrypt(String unEncrypt, String type) {
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance(type);
            messageDigest.update(unEncrypt.getBytes(StandardCharsets.UTF_8));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr.toUpperCase();
    }

    /**
     * 将byte转为16进制
     *
     * @param bytes 待转换数组
     * @return 16进制字符
     */
    private static String byte2Hex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuilder.append("0");
            }
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
}
