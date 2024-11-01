package com.github.nan.web.core.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @author NanNan Wang
 */
public class JasyptUtil {

    public static String encrypt(String text, String password) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(password);
        return encryptor.encrypt(text);
    }

    public static String decrypt(String encryptedText, String password) {
        BasicTextEncryptor encryptor = new BasicTextEncryptor();
        encryptor.setPassword(password);
        return encryptor.decrypt(encryptedText);
    }

    public static String encrypt(String text, String password, String algorithm) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm(algorithm);
        return encryptor.encrypt(text);
    }

    public static String decrypt(String encryptedText, String password, String algorithm) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(password);
        encryptor.setAlgorithm(algorithm);
        return encryptor.decrypt(encryptedText);
    }
}
