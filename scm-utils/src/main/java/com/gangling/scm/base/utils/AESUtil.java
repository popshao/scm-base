package com.gangling.scm.base.utils;


import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 */
@Slf4j
public class AESUtil {

    /**
     * 数据脱敏密钥,长度建议使用16位
     */
    private static final String KEY = "UBnfI6dVZo02HNZX";

    // 加密
    public static String encrypt(String content) {
        if (StringUtil.isEmpty(content)) {
            return content;
        }
        try {
            byte[] raw = KEY.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
            return EncryptUtil.byteArr2HexStr(encrypted);
        } catch (Exception e) {
            log.warn("encrypt error:" + content);
        }
        return content;
    }

    // 解密
    public static String decrypt(String content) {
        try {
            if (StringUtil.isEmpty(content)) {
                return content;
            }
            byte[] raw = KEY.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = EncryptUtil.hexStr2ByteArr(content);
            if (encrypted1 == null) {
                return content;
            }
            try {
                return new String(cipher.doFinal(encrypted1), "utf-8");
            } catch (Exception e) {
                log.warn("AESUtil decrypt error:" + content);
                return content;
            }
        } catch (Exception ex) {
            log.warn("EncryptUtil decrypt error:" + content);
            return content;
        }
    }
}