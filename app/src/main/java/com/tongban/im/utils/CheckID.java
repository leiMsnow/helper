package com.tongban.im.utils;


import java.net.URLEncoder;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;


/**
 * 产生用于判断用户是否登录以及防刷的检查ID
 *
 * @author xuhuahai
 */
public class CheckID {

    private static final byte[] keyBytes = "x1!a0u=+~@v9%^%@io)~7ff*".getBytes();
    private static final String Algorithm = "DESede";
    private static final String Ciper_Algorithm = "DESede/ECB/PKCS5Padding";

    static public class CheckResult {
        private boolean logined = false;
        private boolean expired = true;

        public CheckResult(boolean logined, boolean expired) {
            this.logined = logined;
            this.expired = expired;
        }

        public boolean isLogined() {
            return logined;
        }

        public void setLogined(boolean logined) {
            this.logined = logined;
        }

        public boolean isExpired() {
            return expired;
        }

        public void setExpired(boolean expired) {
            this.expired = expired;
        }

        @Override
        public String toString() {
            return "CheckResult [logined=" + logined + ", expired=" + expired + "]";
        }
    }

    /**
     * 产生ID
     *
     * @param logined
     * @return
     */
    public static String encode(boolean logined) {
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder();
        sb.append(uuid.toString()).append(',');
        if (logined) {
            sb.append('1').append(',');
        } else {
            sb.append('0').append(',');
        }
        sb.append(System.currentTimeMillis());
        try {
            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
            Cipher c1 = Cipher.getInstance(Ciper_Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            byte[] result = c1.doFinal(sb.toString().getBytes("UTF-8"));
            String base64String =  Base64.encodeToString(result,0);
            return URLEncoder.encode(base64String);
        } catch (java.security.NoSuchAlgorithmException e1) {
            return null;
        } catch (javax.crypto.NoSuchPaddingException e2) {
            return null;
        } catch (java.lang.Exception e3) {
            return null;
        }

    }

//    /**
//     * 检查ID
//     *
//     * @param id
//     * @param secs
//     * @return
//     */
//    public static CheckResult check(String id, long secs) {
//        long now = System.currentTimeMillis();
//        try {
//            byte[] sourceBytes = Base64.decodeBase64(id);
//            SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);
//            Cipher c1 = Cipher.getInstance(Algorithm);
//            c1.init(Cipher.DECRYPT_MODE, deskey);
//            byte[] result = c1.doFinal(sourceBytes);
//            String source = new String(result);
//            String[] parts = source.split(",");
//            if (parts.length != 3) {
//                return null;
//            }
//            boolean logined = parts[1].equals("1") ? true : false;
//            boolean expired = Math.abs(now - Long.parseLong(parts[2])) > secs * 1000 ? true : false;
//            return new CheckResult(logined, expired);
//        } catch (java.security.NoSuchAlgorithmException e1) {
//            return null;
//        } catch (javax.crypto.NoSuchPaddingException e2) {
//            return null;
//        } catch (java.lang.Exception e3) {
//            return null;
//        }
//    }


}

