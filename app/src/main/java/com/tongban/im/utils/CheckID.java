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

    public static long difMills = 0;

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
     * @param isLogin     登录标示
     * @param disableCache 缓存标示
     * @return
     */
    public static String encode(boolean isLogin, boolean disableCache) {
        UUID uuid = UUID.randomUUID();
        StringBuilder sb = new StringBuilder();
        sb.append(uuid.toString()).append(",");
        //是否登录标示 true 已经登录；false 未登录
        sb.append(isLogin ? "1" : "0").append(",");
        sb.append(System.currentTimeMillis() + difMills).append(",");
        //是否返回缓存标示 true 获取新数据；false 获取缓存数据
        sb.append(disableCache ? "1" : "0").append(",");
        sb.append(System.currentTimeMillis());
        try {
            SecretKey desKey = new SecretKeySpec(keyBytes, Algorithm);
            Cipher c1 = Cipher.getInstance(Ciper_Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, desKey);
            byte[] result = c1.doFinal(sb.toString().getBytes("UTF-8"));
            String base64String = Base64.encodeToString(result, 0);
            return URLEncoder.encode(base64String);
        } catch (java.security.NoSuchAlgorithmException e1) {
            return null;
        } catch (javax.crypto.NoSuchPaddingException e2) {
            return null;
        } catch (java.lang.Exception e3) {
            return null;
        }

    }

}

