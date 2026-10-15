package ybl.lib.backend.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密工具类
 */
public class MD5Utils {

    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    /**
     * 对字符串进行 MD5 加密，返回 32 位小写十六进制字符串
     *
     * @param text 原文
     * @return 32 位小写 MD5 值
     */
    public static String md5(String text) {
        if (text == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(text.getBytes(StandardCharsets.UTF_8));
            return toHex(digest);
        } catch (NoSuchAlgorithmException e) {
            // JDK 一定支持 MD5，正常不会走到这里
            throw new IllegalStateException("MD5 algorithm not available", e);
        }
    }

    /**
     * 对字节数组进行 MD5 加密
     */
    public static String md5(byte[] data) {
        if (data == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return toHex(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available", e);
        }
    }

    /**
     * 返回 32 位大写 MD5
     */
    public static String md5Upper(String text) {
        String s = md5(text);
        return s == null ? null : s.toUpperCase();
    }

    /**
     * 字节数组转十六进制字符串
     */
    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(HEX_DIGITS[(b >> 4) & 0x0F]);
            sb.append(HEX_DIGITS[b & 0x0F]);
        }
        return sb.toString();
    }
}