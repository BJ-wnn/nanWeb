package com.github.nan.web.core.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author NanNan Wang
 */

public class AESUtil {

    // 设置 AES 模式和填充方式
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    // IV 初始向量（16 字节）
    private static final String IV = "1234567890123456";

    /**
     * 生成 AES 密钥
     * @return Base64 编码的密钥
     */
    public static String generateKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
            keyGen.init(128); // 可以设置为 128、192 或 256 位
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException("Error generating AES key", e);
        }
    }

    /**
     * 加密
     * @param plainText 原文
     * @param base64Key Base64 编码的 AES 密钥
     * @return Base64 编码的密文
     */
    public static String encrypt(String plainText, String base64Key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(base64Key), ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting data", e);
        }
    }

    /**
     * 解密
     * @param cipherText Base64 编码的密文
     * @param base64Key Base64 编码的 AES 密钥
     * @return 解密后的原文
     */
    public static String decrypt(String cipherText, String base64Key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(base64Key), ALGORITHM);
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting data", e);
        }
    }

}

