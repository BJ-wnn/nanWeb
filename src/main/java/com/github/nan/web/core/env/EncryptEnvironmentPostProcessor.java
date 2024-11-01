package com.github.nan.web.core.env;

import com.github.nan.web.core.utils.AESUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.env.*;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.HashMap;

/**
 * @author NanNan Wang
 */
public class EncryptEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private static final String ENC_PREFIX = "ENC(";
    private static final String ENC_SUFFIX = ")";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        MutablePropertySources propertySources = environment.getPropertySources();

        // 遍历每个 PropertySource
        for (PropertySource<?> propertySource : propertySources) {
            String name = propertySource.getName();
            System.out.println(name);
            if (propertySource instanceof MapPropertySource
                && (name.contains("application-") || name.contains("application."))) {
                Map<String, Object> source = (Map<String, Object>) propertySource.getSource();
                Map<String, Object> decryptedProperties = new HashMap<>();

                for (Map.Entry<String, Object> entry : source.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    // 使用 toString() 方法而不是直接强制转换
                    String stringValue = value instanceof OriginTrackedValue
                            ? value.toString()
                            : (value instanceof String ? (String) value : null);
                    if (stringValue != null) {
                        // 检查是否有 ENC() 标记
                        if (StringUtils.hasText(stringValue) && stringValue.startsWith(ENC_PREFIX) && stringValue.endsWith(ENC_SUFFIX)) {
                            String encryptedValue = stringValue.substring(ENC_PREFIX.length(), stringValue.length() - ENC_SUFFIX.length());
                            String decryptedValue = decrypt(encryptedValue);
                            decryptedProperties.put(key, decryptedValue);
                        } else {
                            decryptedProperties.put(key, stringValue); // 保留未加密的值
                        }
                    } else if (value instanceof String[]) {
                        // 处理数组情况
                        String[] values = (String[]) value;
                        for (int i = 0; i < values.length; i++) {
                            String valueStr = values[i].toString(); // 确保调用 toString()
                            if (StringUtils.hasText(valueStr) && valueStr.startsWith(ENC_PREFIX) && valueStr.endsWith(ENC_SUFFIX)) {
                                String encryptedValue = valueStr.substring(ENC_PREFIX.length(), valueStr.length() - ENC_SUFFIX.length());
                                String decryptedValue = decrypt(encryptedValue);
                                values[i] = decryptedValue;
                            }
                        }
                        decryptedProperties.put(key, values);
                    }
                }
                // 要注意 替换原来的 而不是添加到first or last ，否则 可能会有问题
                propertySources.replace(name, new MapPropertySource(name, decryptedProperties));
            }
        }
    }

    private String decrypt(String encryptedText) {
        // 替换成实际的解密逻辑
        return AESUtil.decrypt(encryptedText, "IGJ+xzgyBYMoQQJ6YYa6OQ==");
    }
}
