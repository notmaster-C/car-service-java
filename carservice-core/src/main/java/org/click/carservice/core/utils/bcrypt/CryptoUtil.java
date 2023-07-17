package org.click.carservice.core.utils.bcrypt;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;

import java.util.Base64;

/**
 * iUwaIDfcdto=
 * 加密解密
 *
 * @author click
 */
public class CryptoUtil {

    /**
     * TODO 请勿修改，修改后多租户需要重新配置
     * 密钥，用于加密解密
     */
    private static final String SECRET_KEY = "iUwaIDfcdto=";

    /**
     * 生成密文
     *
     * @param info 明文
     * @return 密文
     */
    public static String encrypt(String info) {
        return encrypt(info, SECRET_KEY);
    }

    /**
     * 生成密文
     *
     * @param info 明文
     * @return 密文
     */
    public static String encrypt(String info, String secretKey) {
        byte[] key = new byte[0];
        Base64.Decoder decoder = Base64.getDecoder();
        key = decoder.decode(secretKey);
        DES des = SecureUtil.des(key);
        return des.encryptHex(info);
    }


    /**
     * 密文解明文
     *
     * @param encrypt 密文
     * @return 明文
     */
    public static String decode(String encrypt) {
        return decode(encrypt, SECRET_KEY);
    }

    /**
     * 密文解明文
     *
     * @param encrypt   密文
     * @param secretKey 密钥
     * @return 明文
     */
    public static String decode(String encrypt, String secretKey) {
        byte[] key = new byte[0];
        key = Base64.getDecoder().decode(secretKey);
        DES des = SecureUtil.des(key);
        String decryptStr;
        try {
            decryptStr = des.decryptStr(encrypt);
        } catch (Exception e) {
            return null;
        }
        return decryptStr;
    }


    public static void main(String[] args) {
        //生成密钥
        String encrypt = CryptoUtil.encrypt("123456", "wxf6b79fa76acf071c");
        System.out.println("密文：" + encrypt);
        String decode = CryptoUtil.decode(encrypt);
        System.out.println("明文：" + decode);
    }

}
