package com.why.baseframework.util;

import com.why.baseframework.constants.ReflectConstants;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenglin.wu
 * @description:
 * @title: DeclaredAndSuperClass
 * @projectName WHY-Core
 * @date 2021/8/10
 * @company 上海织田信息技术有限公司 (Oda-Group)
 */
public class DeclaredAndSuperClass {
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
    public List<Field> getAllFields() {
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
    public List<Field> getDeclaredFields() {
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
    public List<Field> getSuperFields() {
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
    public List<Method> getSuperAllGetter() {

        return this.getAllGetter(this.superClass);
    }

    /**
     * 获取父类的所有的getter方法
     *
     * @return List<Method>
     * @author chenglin.wu
     * @date: 2021/4/27
     */
    public List<Method> getAllGetter() {

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
    public List<Method> getAllGetter(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        return Arrays.stream(methods)
                .filter(method -> method.getName().startsWith(ReflectConstants.GET_PREFIX) && !method.getName().equals(ReflectConstants.GET_PARAMS))
                .collect(Collectors.toList());
    }
}
