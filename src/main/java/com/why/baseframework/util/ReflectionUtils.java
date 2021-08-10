package com.why.baseframework.util;

import com.why.baseframework.constants.IntConstants;
import com.why.baseframework.constants.ReflectConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Author chenglin.wu
 * @Description:
 * @Title: ReflectionUtils
 * @Date 2021/4/16 10:57
 * @Company WHY-Group
 */
public final class ReflectionUtils {
    private static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);
    /**
     * 初始化基本数据类型和包装类型的simpleName
     */
    public static final List<String> DATA_TYPE_LIST = new ArrayList<>(IntConstants.INT_17);

    static {
        DATA_TYPE_LIST.add(ReflectConstants.BYTE);
        DATA_TYPE_LIST.add(ReflectConstants.INT);
        DATA_TYPE_LIST.add(ReflectConstants.CHAR);
        DATA_TYPE_LIST.add(ReflectConstants.LONG);
        DATA_TYPE_LIST.add(ReflectConstants.DOUBLE);
        DATA_TYPE_LIST.add(ReflectConstants.BOOLEAN);
        DATA_TYPE_LIST.add(ReflectConstants.FLOAT);
        DATA_TYPE_LIST.add(ReflectConstants.SHORT);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_BYTE);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_INT);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_CHAR);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_LONG);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_DOUBLE);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_FLOAT);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_SHORT);
        DATA_TYPE_LIST.add(ReflectConstants.PACKING_BOOLEAN);
        DATA_TYPE_LIST.add(ReflectConstants.STRING_SIMPLE_NAME);
    }

    /**
     * 私有化
     */
    private ReflectionUtils() {
    }

    /**
     * 获取泛型的实际类型
     *
     * @param clazz 泛型的class对象
     * @return Class 返回实际的Class对象
     * @author chenglin.wu
     * @date: 2021/4/16
     */
    @SuppressWarnings("rawtypes")
    public static Class getSuperClassGenericType(Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 获取set方法映射表
     *
     * @param targetMethods 反射拿到的所有的方法
     * @return HashMap<String, Method> 返回set方法的映射表
     * @author chenglin.wu
     * @date: 2021/4/16
     */
    public static HashMap<String, Method> getMethodMap4Set(Method[] targetMethods) {
        return ReflectionUtils.getSetMethodMapping(targetMethods);
    }

    /**
     * 通过实体获取实体的父类Class对象和自己的Class文件对象
     *
     * @param entity 实体类
     * @return DeclaredAndSuperClass
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    public static <T> DeclaredAndSuperClass getDeclaredAndSuperClass(T entity) {
        if (ObjectUtils.anyNull(entity)) {
            throw new IllegalArgumentException("null");
        }
        return new DeclaredAndSuperClass(entity.getClass());
    }

    /**
     * 判断此属性是否为忽略属性
     *
     * @param field 属性
     * @return boolean false 为忽略字段 true 则不忽略
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    public static boolean judgementIgnoreField(Field field) {
        Annotation[] annotations = field.getAnnotations();
        // 查找所有当前属性上的所有注解，并判断是否被忽略
        for (Annotation annotation : annotations) {
            if (annotation instanceof Transient){
                return true;
            }
        }
        // 获取属性的关键字
        int modifiers = field.getModifiers();
        // 如果为staic 或者final 修饰的字段则直接忽略
        return Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers);
    }

    /**
     * 通过实体类的属性获取mongoDb的数据库字段名
     *
     * @param field 实体类的属性
     * @return String
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    public static String getTranslationWithSeparatorFieldName(Field field){
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof org.springframework.data.mongodb.core.mapping.Field){
                return ((org.springframework.data.mongodb.core.mapping.Field) annotation).name();
            }
        }
        return StrChartUtil.toLowerCaseTranslationStr(field.getName(),null);
    }


    /**
     * @param targetMethods 所有的方法数组
     * @return HashMap<String, Method>
     * @author chenglin.wu
     * @date: 2021/4/16
     */
    private static HashMap<String, Method> getSetMethodMapping(Method[] targetMethods) {
        HashMap<String, Method> myMethods = new HashMap<>();
        if (targetMethods == null || targetMethods.length <= 0) {
            return myMethods;
        }
        // 将所有set方法加入到映射表中
        for (Method method : targetMethods) {
            if (method.getName().startsWith("set")) {
                myMethods.put(method.getName(), method);
            }
        }
        return myMethods;
    }

    /**
     * 获取泛型的实际类型
     *
     * @param clazz 泛型的class对象
     * @param index
     * @return Class
     * @author chenglin.wu
     * @date: 2021/4/16
     */
    @SuppressWarnings("rawtypes")
    private static Class getSuperClassGenericType(Class clazz, int index) {
        // 获取当前class字节码对象的直接父类包括当前类的泛型
        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        // 获取泛型的class字节码类型
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        // 如果下标超过了,就直接返回Object的字节码对象
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        // 如果获取出来的不是字节码对象,也是返回Object的字节码对象
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        // 将获取到的字节码对象强转到指定类型
        return (Class) params[index];
    }



}
