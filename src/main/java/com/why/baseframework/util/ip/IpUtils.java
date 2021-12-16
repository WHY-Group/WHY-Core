package com.why.baseframework.util.ip;


import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 获取IP方法
 *
 * @author WHY
 */
public class IpUtils {
    /**
     * 获取请求的IP地址
     *
     * @param request httpServletRequest
     * @return String
     * @author chenglin.wu
     * @date: 2021/5/6
     */
    public static String getIpAddr(HttpServletRequest request) {
        if (request == null) {
            return IpConstants.UNKNOWN;
        }
        String ip = request.getHeader(IpConstants.X_FORWARDED_FOR);
        if (StringUtils.isBlank(ip) || IpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(IpConstants.PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || IpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(IpConstants.X_FORWARDED_FOR_UPPER);
        }
        if (StringUtils.isBlank(ip) || IpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(IpConstants.WL_PROXY_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || IpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(IpConstants.X_REAL_IP);
        }
        if (StringUtils.isBlank(ip) || IpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(IpConstants.HTTP_CLIENT_IP);
        }
        if (StringUtils.isBlank(ip) || IpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(IpConstants.HTTP_X_FORWARDED_FOR);
        }

        if (StringUtils.isBlank(ip) || IpConstants.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return IpConstants.IPV6_LOCALHOST.equals(ip) ? IpConstants.LOCALHOST : ip;
    }

    public static boolean internalIp(String ip) {
        byte[] addr = textToNumericFormatV4(ip);
        return internalIp(addr) || IpConstants.LOCALHOST.equals(ip);
    }

    private static boolean internalIp(byte[] addr) {
        if (ObjectUtils.allNull(addr) || ObjectUtils.isEmpty(addr) || addr.length < 2) {
            return true;
        }
        final byte b0 = addr[0];
        final byte b1 = addr[1];
        // 10.x.x.x/8
        final byte section1 = 0x0A;
        // 172.16.x.x/12
        final byte section2 = (byte) 0xAC;
        final byte section3 = (byte) 0x10;
        final byte section4 = (byte) 0x1F;
        // 192.168.x.x/16
        final byte section5 = (byte) 0xC0;
        final byte section6 = (byte) 0xA8;
        switch (b0) {
            case section1:
                return true;
            case section2:
                if (b1 >= section3 && b1 <= section4) {
                    return true;
                }
            case section5:
                switch (b1) {
                    case section6:
                        return true;
                }
            default:
                return false;
        }
    }

    /**
     * 将IPv4地址转换成字节
     *
     * @param text IPv4地址
     * @return byte 字节
     */
    public static byte[] textToNumericFormatV4(String text) {
        if (text.length() == 0) {
            return null;
        }

        byte[] bytes = new byte[4];
        String[] elements = text.split("\\.", -1);
        try {
            long l;
            int i;
            switch (elements.length) {
                case 1:
                    l = Long.parseLong(elements[0]);
                    if ((l < 0L) || (l > 4294967295L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l >> 24 & 0xFF);
                    bytes[1] = (byte) (int) ((l & 0xFFFFFF) >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 2:
                    l = Integer.parseInt(elements[0]);
                    if ((l < 0L) || (l > 255L)) {
                        return null;
                    }
                    bytes[0] = (byte) (int) (l & 0xFF);
                    l = Integer.parseInt(elements[1]);
                    if ((l < 0L) || (l > 16777215L)) {
                        return null;
                    }
                    bytes[1] = (byte) (int) (l >> 16 & 0xFF);
                    bytes[2] = (byte) (int) ((l & 0xFFFF) >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 3:
                    for (i = 0; i < 2; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    l = Integer.parseInt(elements[2]);
                    if ((l < 0L) || (l > 65535L)) {
                        return null;
                    }
                    bytes[2] = (byte) (int) (l >> 8 & 0xFF);
                    bytes[3] = (byte) (int) (l & 0xFF);
                    break;
                case 4:
                    for (i = 0; i < 4; ++i) {
                        l = Integer.parseInt(elements[i]);
                        if ((l < 0L) || (l > 255L)) {
                            return null;
                        }
                        bytes[i] = (byte) (int) (l & 0xFF);
                    }
                    break;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
        return bytes;
    }

    public static String getHostIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException ignored) {
        }
        return IpConstants.LOCALHOST;
    }

    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
        }
        return IpConstants.UNKNOWN;
    }

    /**
     * 关于IP的常量方法
     */
    private static class IpConstants {
        /**
         * 未知的
         */
        private static final String UNKNOWN = "unknown";
        /**
         * 本机内网IP
         */
        private static final String LOCALHOST = "127.0.0.1";
        /**
         * ipv6的本机IP
         */
        private static final String IPV6_LOCALHOST = "0:0:0:0:0:0:0:1";

        // header
        /**
         * 转发
         */
        private static final String X_FORWARDED_FOR = "x-forwarded-for";
        /**
         * 某些部分大写的转发
         */
        private static final String X_FORWARDED_FOR_UPPER = "X-Forwarded-For";
        /**
         * wl 代理服务器
         */
        private static final String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
        /**
         * 代理服务器代理的IP
         */
        private static final String PROXY_CLIENT_IP = "Proxy-Client-IP";
        /**
         * 真是IP
         */
        private static final String X_REAL_IP = "X-Real-IP";
        /**
         * HTTP_CLIENT_IP
         */
        private static final String HTTP_CLIENT_IP = "HTTP_CLIENT_IP";
        /**
         * HTTP_X_FORWARDED_FOR
         */
        private static final String HTTP_X_FORWARDED_FOR = "HTTP_X_FORWARDED_FOR";

    }

}