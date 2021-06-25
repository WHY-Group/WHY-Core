package com.why.baseframework.util;

import com.why.baseframework.base.web.exception.BusinessException;
import com.why.baseframework.enums.ErrCodeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @Author chenglin.wu
 * @Description:
 * @Title: StrChartSetUtil
 * @ProjectName base_framework
 * @Date 2021/5/7
 * @Company  WHY-Group
 */
@Deprecated
public final class StrChartSetUtil {
    /**
     * 私有化构造器
     */
    private StrChartSetUtil() {

    }

    /**
     * 判断字符串的常用编码
     *
     * @param str 字符串
     * @return String
     * @author chenglin.wu
     * @date: 2021/5/7
     */
    public static String getEncoding(final String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex2) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex3) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex4) {
        }
        encode = "US-ASCII";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex5) {
        }
        encode = "UTF-16BE";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex6) {
        }
        encode = "UTF-16LE";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex7) {
        }
        encode = "UTF-16";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex8) {
        }
        encode = "GB18030";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex9) {
        }
        encode = "UNICODE";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex10) {
        }
        encode = "BIG5";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception ex11) {
        }
        return StringUtils.EMPTY;
    }

    public static String change2UTF8(String str) throws BusinessException, UnsupportedEncodingException {
        String encoding = getEncoding(str);
        if (StringUtils.isBlank(encoding)) {
            throw new BusinessException(ErrCodeEnum.UNKNOWN.getCode(), "未知的字符串编码格式");
        }
        return new String(str.getBytes(encoding), StandardCharsets.UTF_8);
    }


}
