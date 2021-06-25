package com.why.baseframework.constants;

/**
 * @Author chenglin.wu
 * @Description:
 * @Title: ReflectMethodPrefix
 * @ProjectName base_framework
 * @Date 2021/4/16 13:18
 * @Company  WHY-Group
 */
public final class ReflectConstants {

    /**
     * 私有化常量类的构造器
     */
    private ReflectConstants() {
    }

    /**
     * set方法的前缀
     */
    public static final String SET_PREFIX = "set";

    /**
     * get方法的前缀
     */
    public static final String GET_PREFIX = "get";

    /**
     * boolean方法获取值的前缀
     */
    public static final String IS_PREFIX = "is";
    // ---------------------------------------------------
    //    基本数据类型和其包装类的字节码对象可能拿到的simpleName
    // ---------------------------------------------------
    // 基本数据类型
    /**
     * int
     */
    public static final String INT = "int";
    /**
     * char
     */
    public static final String CHAR = "char";
    /**
     * long
     */
    public static final String LONG = "long";
    /**
     * double
     */
    public static final String DOUBLE = "double";
    /**
     * float
     */
    public static final String FLOAT = "float";
    /**
     * boolean
     */
    public static final String BOOLEAN = "boolean";
    /**
     * byte
     */
    public static final String BYTE = "byte";
    /**
     * short
     */
    public static final String SHORT = "short";
    // 包装类
    /**
     * Integer
     */
    public static final String PACKING_INT = "Integer";
    /**
     * Character
     */
    public static final String PACKING_CHAR = "Character";
    /**
     * Long
     */
    public static final String PACKING_LONG = "Long";
    /**
     * Double
     */
    public static final String PACKING_DOUBLE = "Double";
    /**
     * Float
     */
    public static final String PACKING_FLOAT = "Float";
    /**
     * Byte
     */
    public static final String PACKING_BYTE = "Byte";
    /**
     * Boolean
     */
    public static final String PACKING_BOOLEAN = "Boolean";
    /**
     * Short
     */
    public static final String PACKING_SHORT = "Short";
    /**
     * String
     */
    public static final String STRING_SIMPLE_NAME = "String";
    /**
     * BaseEntity的方法 getParams名称
     */
    public static final String GET_PARAMS = "getParams";


}
