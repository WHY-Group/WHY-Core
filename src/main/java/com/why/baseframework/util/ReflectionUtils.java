package com.why.baseframework.util;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.why.baseframework.base.entity.BaseEntity;
import com.why.baseframework.constants.IntConstants;
import com.why.baseframework.constants.ReflectConstants;
import com.why.baseframework.constants.WrapperConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

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
    private static final List<String> DATA_TYPE_LIST = new ArrayList<>(IntConstants.INT_17);

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
     * 通过当前实体类的数据获取查询的queryWrapper
     *
     * @param entity 守实体类
     * @return QueryWrapper<T>
     * @throws NoSuchMethodException,InvocationTargetException,IllegalAccessException
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    public static <T extends BaseEntity> QueryWrapper<T> createPageWrapper2Field(T entity, QueryWrapper<T> wrapper) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        DeclaredAndSuperClass declaredAndSuperClass = getDeclaredAndSuperClass(entity);
        List<Field> allFields = declaredAndSuperClass.getAllFields();
        // 遍历属性
        for (Field field : allFields) {
            // 属性类型
            String fieldType = field.getType().getSimpleName();
            boolean notIgnore = judgementIgnoreField(field);
            if (DATA_TYPE_LIST.contains(fieldType) && notIgnore) {
                String getMethodName = ReflectConstants.GET_PREFIX + upperCaseFirst(field.getName());
                try {
                    Method method = declaredAndSuperClass.getDeclaredClass().getMethod(getMethodName);
                    getWrapper(entity, field, method, wrapper);
                } catch (NoSuchMethodException e) {
                    Method method = declaredAndSuperClass.getSuperClass().getMethod(getMethodName);
                    getWrapper(entity, field, method, wrapper);
                    log.error("child class no this method name :{}", getMethodName);
                } catch (IllegalAccessException e) {
                    log.error("method : {} not access permission ", getMethodName);
                } catch (InvocationTargetException e) {
                    log.error("method ：{} internal exception", getMethodName);
                }
            }
        }
//        try {
//            getExtendField(entity, declaredAndSuperClass.getSuperClass(), wrapper);
//        } catch (Exception e) {
//            log.error("create extend field exception");
//        }
        return wrapper;
    }
    /**
     * 通过实体获取实体的父类Class对象和自己的Class文件对象
     *
     * @param entity 实体类
     * @return DeclaredAndSuperClass
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static <T> DeclaredAndSuperClass getDeclaredAndSuperClass(T entity) {
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
    private static boolean judgementIgnoreField(Field field) {
        // 获取属性中所有的注解
        TableField tableField = field.getAnnotation(TableField.class);
        if (ObjectUtils.anyNull(tableField)) {
            int modifiers = field.getModifiers();
            // 如果为staic 或者final 修饰的字段则直接忽略
            return !(Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers));
        }
        return tableField.exist();
    }

    /**
     * 构造wrapper
     *
     * @param entity  实体类
     * @param field   属性
     * @param method  get 方法
     * @param wrapper new 出来的wrapper
     * @return void
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static <T extends BaseEntity> void getWrapper(T entity, Field field, Method method, QueryWrapper<T> wrapper) throws IllegalAccessException, InvocationTargetException {
        Object invoke = method.invoke(entity);
        if (ObjectUtils.anyNull(invoke)) {
            return;
        }
        String columnName = getTableFieldAnnotationValue(field);
        // 构造查询条件
        if (ReflectConstants.STRING_SIMPLE_NAME.equals(field.getType().getSimpleName()) && ObjectUtils.isNotEmpty(invoke)) {
            wrapper.like(columnName, invoke);
        } else if (ObjectUtils.isNotEmpty(invoke)) {
            wrapper.eq(columnName, invoke);
        }
    }


    /**
     * 设置查询的创建时间范围
     *
     * @param params
     * @Return void
     * @Author Y
     * @Date 2021/4/29 15:11
     **/
    public static void setBetweenMap(Map<String, Object> params) throws Exception {
        if (ObjectUtils.anyNull(params)) {
            return;
        }
        Object startTime = params.get(WrapperConstants.BEGIN_TIME);
        Object endTime = params.get(WrapperConstants.END_TIME);
        if (ObjectUtils.anyNull(startTime)) {
            //从本月月初开始查询
//            params.put(WrapperConstants.BEGIN_TIME, DateU.m(new Date()));
        } else {
            params.put(WrapperConstants.BEGIN_TIME, DateTimeUtils.ObjectToDate(params.get(WrapperConstants.BEGIN_TIME)));
        }
        if (ObjectUtils.anyNull(endTime)) {
            params.put(WrapperConstants.END_TIME, new Date());
        } else {
            params.put(WrapperConstants.END_TIME, DateTimeUtils.ObjectToDate(params.get(WrapperConstants.END_TIME)));
        }
    }

    /**
     * 获取扩展字段的东西
     *
     * @param entity  实体类
     * @param aClass  父类的class对象
     * @param wrapper queryWrapper
     * @return void
     * @throws Exception
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static <T extends BaseEntity> void getExtendField(T entity, Class<?> aClass, QueryWrapper<T> wrapper) throws Exception {
        Method method = aClass.getMethod(ReflectConstants.GET_PARAMS);
        Object invoke = null;
        try {
            invoke = method.invoke(entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (ObjectUtils.allNotNull(invoke)) {
            Map<String, Object> betweenMap = (Map<String, Object>) invoke;
            //设置筛选时间
            setBetweenMap(betweenMap);
            //设置筛选时间字段
            wrapper.between(WrapperConstants.CREATE_TIME,
                    betweenMap.get(WrapperConstants.BEGIN_TIME),
                    betweenMap.get(WrapperConstants.END_TIME));
        }
    }


    /**
     * 获取属性对应的数据库列名
     *
     * @param field 属性
     * @return String
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static String getTableFieldAnnotationValue(Field field) {
        TableField annotation = field.getAnnotation(TableField.class);
        if (ObjectUtils.allNotNull(annotation) && StringUtils.isNotBlank(annotation.value())) {
            return annotation.value();
        } else {
            return getDbField(field.getName());
        }
    }



    /**
     * 将字符串的首字母小写
     *
     * @param str 传递就来的字符串
     * @return 返回首字母小写后的字符串
     */
    public static String lowerCaseFirst(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 首字母小写
        char[] ch = str.toCharArray();
        if (ch[0] >= 'A' && ch[0] <= 'Z') {
            ch[0] = (char) (ch[0] + 32);
        }
        return new String(ch);
    }

    /**
     * 将字符串的首字母大写
     *
     * @param str 传递就来的字符串
     * @return 返回首字母大写后的字符串
     */
    public static String upperCaseFirst(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        // 首字母小写
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 构造wrapper
     *
     * @param wrapper   new 出来的wrapper
     * @param fieldName 字段名
     * @param invoke    调用get方法获取出来的值
     * @param field     数据库字段名
     * @return void
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static <T extends BaseEntity> void getWrapper(QueryWrapper<T> wrapper, String fieldName, Object invoke, String field) {
        // 构造查询条件
        if (ReflectConstants.STRING_SIMPLE_NAME.equals(fieldName) && ObjectUtils.allNotNull(invoke)) {
            wrapper.like(field, invoke);
        } else if (ObjectUtils.isNotEmpty(invoke)) {
            wrapper.eq(field, invoke);
        }
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




    /**
     * 获取数据库的字段名 转换成全大写
     *
     * @param fieldStr 当前的数据库字段名
     * @return String
     * @author chenglin.wu
     * @date: 2021/4/27
     */
    private static String getDbField(String fieldStr) {
        StringBuilder sb = new StringBuilder();
        char[] chars = fieldStr.toCharArray();
        for (char aChar : chars) {
            if (aChar >= IntConstants.INT_65 && aChar <= IntConstants.INT_90) {
                sb.append('_');
            }
            sb.append(aChar);
        }
        String tableField = sb.toString();
        return tableField.toUpperCase();
    }

    private static class DeclaredAndSuperClass {

        private final Class<?> declaredClass;

        private final Class<?> superClass;

        public DeclaredAndSuperClass(Class<?> declaredClass) {
            this.declaredClass = declaredClass;
            this.superClass = declaredClass.getSuperclass();
        }

        public Class<?> getDeclaredClass() {
            return declaredClass;
        }

        public Class<?> getSuperClass() {
            return superClass;
        }

        /**
         * 获取本类和父类所有的属性
         *
         * @return List<Field>
         * @author chenglin.wu
         * @date: 2021/5/17
         */
        private List<Field> getAllFields() {
            List<Field> fields = new ArrayList<>();
            Field[] declaredFields = this.declaredClass.getDeclaredFields();
            Field[] superFields = this.superClass.getDeclaredFields();
            fields.addAll(Arrays.asList(superFields));
            fields.addAll(Arrays.asList(declaredFields));
            return fields;
        }

        /**
         * 获取本类的所有属性
         *
         * @return List<Field>
         * @author chenglin.wu
         * @date: 2021/5/17
         */
        private List<Field> getDeclaredFields() {
            Field[] declaredFields = this.declaredClass.getDeclaredFields();
            return new ArrayList<>(Arrays.asList(declaredFields));
        }

        /**
         * 获取父类的所有属性
         *
         * @return List<Field>
         * @author chenglin.wu
         * @date: 2021/5/17
         */
        private List<Field> getSuperFields() {
            Field[] declaredFields = this.superClass.getDeclaredFields();
            return new ArrayList<>(Arrays.asList(declaredFields));
        }
        /**
         * 获取父类的所有的getter方法
         *
         * @return List<Method>
         * @author chenglin.wu
         * @date: 2021/4/27
         */
        private List<Method> getSuperAllGetter() {

            return this.getAllGetter(this.superClass);
        }

        /**
         * 获取父类的所有的getter方法
         *
         * @return List<Method>
         * @author chenglin.wu
         * @date: 2021/4/27
         */
        private List<Method> getAllGetter() {

            return this.getAllGetter(this.declaredClass);
        }

        /**
         * 获取当前类或者父类的所有的getter方法
         *
         * @param clazz 当前entity的class对象
         * @return List<Method>
         * @author chenglin.wu
         * @date: 2021/4/27
         */
        private List<Method> getAllGetter(Class<?> clazz) {
            Method[] methods = clazz.getDeclaredMethods();
            return Arrays.stream(methods)
                    .filter(method -> method.getName().startsWith(ReflectConstants.GET_PREFIX) && !method.getName().equals(ReflectConstants.GET_PARAMS))
                    .collect(Collectors.toList());
        }
    }

}
