package com.why.baseframework.util;


import com.why.baseframework.constants.IntConstants;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @Author W
 * @Description:
 * @Title: ReflectionUtils
 * @Date 2021/4/22
 * @Company  WHY-Group
 */
public final class EncryptUtils {

    /**
     * 算法
     */
    private static final String AALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 默认AES加密解密Key
     */
    private static final String DEFAULT_AES_KEY = "SkyeKu0lFaAjV8pC";

    /**
     * 私有化构造器
     */
    private EncryptUtils() {

    }

    /**
     * 计算秘钥
     *
     * @param strFix 计算秘钥字符串
     * @return String
     * @author W
     * @date: 2021/4/22
     */
    public static String getEncryptKey(final String strFix) {
        if (StringUtils.isBlank(strFix)) {
            return DEFAULT_AES_KEY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < DEFAULT_AES_KEY.length(); i++) {
            // 部分位替换
            if (i % 2 == 0 && i < strFix.length()) {
                int j = strFix.length() - i - 1;
                sb.append(strFix.substring(j, j + 1));
            } else {
                sb.append(DEFAULT_AES_KEY.substring(i, i + 1));
            }
        }
        return sb.toString();
    }

    /**
     * 加密返回字符串
     *
     * @param content    内容
     * @param encryptKey 秘钥
     * @return String
     * @author W
     * @date: 2021/4/22
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return base64Encode(aesEncryptToBytes(content, encryptKey));
    }

    /**
     * 加密返回字节
     *
     * @param content    内容
     * @param encryptKey 秘钥
     * @return byte[]
     * @author W
     * @date: 2021/4/22
     */
    private static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(IntConstants.INT_128);
        Cipher cipher = Cipher.getInstance(AALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "AES"));
        return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解密
     *
     * @param encryptStr 密文字符串
     * @param decryptKey 秘钥
     * @return String
     * @author W
     * @date: 2021/4/22
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
    }

    /**
     * 解密
     *
     * @param encryptBytes 密文字节
     * @param decryptKey   秘钥
     * @return String
     * @author W
     * @date: 2021/4/22
     */
    private static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(IntConstants.INT_128);

        Cipher cipher = Cipher.getInstance(AALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(StandardCharsets.UTF_8), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);

        return new String(decryptBytes, StandardCharsets.UTF_8);
    }

    /**
     * Base64编码
     *
     * @param bytes 字节
     * @return String
     * @author W
     * @date: 2021/4/22
     */
    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * Base64解码
     *
     * @param base64Code 编码字符串
     * @return byte[]
     * @author W
     * @date: 2021/4/22
     */
    private static byte[] base64Decode(String base64Code) throws Exception {
        return Base64.getDecoder().decode(base64Code);
    }

    /**
     * sha1加密
     *
     * @param str 明文字符串
     * @return String
     * @author W
     * @date: 2021/4/22
     */
    public static String toSha1(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
