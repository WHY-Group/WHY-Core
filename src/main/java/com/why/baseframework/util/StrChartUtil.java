package com.why.baseframework.util;

import com.why.baseframework.base.web.exception.BusinessException;
import com.why.baseframework.constants.IntConstants;
import com.why.baseframework.enums.ErrCodeEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @Author chenglin.wu
 * @Description:
 * @Title: StrChartUtil
 * @ProjectName base_framework
 * @Date 2021/5/7
 * @Company  WHY-Group
 */
public final class StrChartUtil {
    /**
     * 默认根据驼峰切割字符串的填充
     * <p>
     * strTran -- str_Tran
     */
    private static final String DEFAULT_SEPARATOR = "_";
    /**
     * 私有化构造器
     */
    private StrChartUtil() {

    }

//    /**
//     * 判断字符串的常用编码 针对纯英文、纯数字、英文加数字的组合不生效，只有加入汉字的时候生效
//     *
//     * @param str 字符串
//     * @return String
//     * @author chenglin.wu
//     * @date: 2021/5/7
//     */
//    public static String getEncoding(final String str) {
//        String encode = "UTF-8";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex) {
//        }
//        encode = "ISO-8859-1";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex2) {
//        }
//        encode = "GB2312";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex3) {
//        }
//        encode = "GBK";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex4) {
//        }
//        encode = "US-ASCII";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex5) {
//        }
//        encode = "UTF-16BE";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex6) {
//        }
//        encode = "UTF-16LE";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex7) {
//        }
//        encode = "UTF-16";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex8) {
//        }
//        encode = "GB18030";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex9) {
//        }
//        encode = "UNICODE";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex10) {
//        }
//        encode = "BIG5";
//        try {
//            if (str.equals(new String(str.getBytes(encode)))) {
//                return encode;
//            }
//        } catch (Exception ex11) {
//        }
//        return StringUtils.EMPTY;
//    }
//
//    public static String change2UTF8(String str) throws BusinessException, UnsupportedEncodingException {
//        String encoding = getEncoding(str);
//        if (StandardCharsets.UTF_8.toString().equals(encoding)){
//            return str;
//        }
//        if (StringUtils.isBlank(encoding)) {
//            throw new BusinessException(ErrCodeEnum.UNKNOWN.getCode(), "未知的字符串编码格式");
//        }
//        return new String(str.getBytes(encoding), StandardCharsets.UTF_8);
//    }

    /**
     * 将分割出来的字符串全大写
     * 例： param1： "strTran"  param2： "_"   result: "STR_TRAN"
     *
     * @param translationStr the translationStr 需要转换的字符串
     * @param separator      the separator 分割符
     * @return String
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    public static String toUpperCaseTranslationStr(String translationStr,String separator){
        String str = translationStrWithSeparator(translationStr, separator);
        return str.toUpperCase();
    }

    /**
     * 将分割出来的字符串转为全小写
     * 例： param1： "strTran"  param2： "_"   result: "str_tran"
     *
     * @param translationStr the translationStr 需要转换的字符串
     * @param separator      the separator 分割符
     * @return String
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    public static String toLowerCaseTranslationStr(String translationStr,String separator){
        String str = translationStrWithSeparator(translationStr, separator);
        return str.toLowerCase();
    }


    /**
     * 传入驼峰的字符串，通过驼峰来辨别从哪开始分割
     * <p>
     * 例： param1： "strTran"  param2： "_"   result: "str_Tran"
     * param1： "StrTran" ; param2： "_"   result: "Str_Tran"
     * param1： "str"  param2： "_"   result: "str"
     *
     * @param translationStr 需要转换的字符串
     * @param separator      分割符
     * @return String
     * @author chenglin.wu
     * @date: 2021/4/27
     */
    private static String translationStrWithSeparator(String translationStr,String separator) {
        StringBuilder sb = new StringBuilder();
        char[] chars = translationStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            // 如果当前char字符为大写，则在中间添加切割
            if (aChar >= IntConstants.INT_65 && aChar <= IntConstants.INT_90 && i != 0) {
                sb.append(StringUtils.isBlank(separator) ? DEFAULT_SEPARATOR : separator);
            }
            sb.append(aChar);
        }
        return sb.toString();
    }


    /**
     * 将字符串的首字母小写
     * 例： Str   result: str
     *
     * @param str 传递就来的字符串
     * @return 返回首字母小写后的字符串
     */
    public static String lowerCaseFirst(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        char upperFirst = 'A';
        char upperLast = 'Z';
        // 首字母小写
        char[] ch = str.toCharArray();
        if (ch[0] >= upperFirst && ch[0] <= upperLast) {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

    /**
     * 将字符串的首字母大写
     * 例： str   result: Str
     *
     * @param str 传递就来的字符串
     * @return 返回首字母大写后的字符串
     */
    public static String upperCaseFirst(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        char lowerFirst = 'a';
        char lowerLast = 'z';
        // 首字母小写
        char[] ch = str.toCharArray();
        if (ch[0] >= lowerFirst && ch[0] <= lowerLast) {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }
}
