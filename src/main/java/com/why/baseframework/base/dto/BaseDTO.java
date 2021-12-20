package com.why.baseframework.base.dto;

import com.why.baseframework.base.entity.BaseEntity;
import com.why.baseframework.constants.ReflectMethodPrefix;
import com.why.baseframework.util.ReflectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author W
 * @Description:
 * @Title: BaseDto
 * @ProjectName base_framework
 * @Date 2021/4/16 10:24
 * @Company WHY-Group
 */
@Slf4j
@SuppressWarnings("rawtypes")
public abstract class BaseDTO<E extends BaseEntity> implements Serializable {
    /**
     * 序列化version
     */
    private static final long serialVersionUID = 3363466718882617271L;

    /**
     * 对应entity的类型
     */
    @Transient
    protected Class<E> entityClass;

    /**
     * entity基类的构造函数，重点是设置entity对应dto的类型
     */
    @SuppressWarnings("unchecked")
    public BaseDTO() {
        this.entityClass = ReflectionUtils.getSuperClassGenericType(getClass());
    }

    /**
     * 将DTO转换成Entity时只转换基本类型或其包装类，加String类型不转换复杂的类型,需要注意同名问题
     *
     * @param src    需要转换的对象
     * @param target 转换成功的对象
     * @return Object
     * @author W
     * @date: 2021/4/16
     */
    public static Object populate(Object src, Object target) {
        Method[] srcMethods = src.getClass().getMethods();
        Method[] targetMethods = target.getClass().getMethods();
        // 设置方法映射表
        HashMap<String, Method> targetSetMethods = ReflectionUtils.getMethodMap4Set(targetMethods);
        // 遍历源对象的所有get方法
        for (Method m : srcMethods) {

            String srcName = m.getName();
            // 获取get方法进行设值处理
            if (srcName.startsWith(ReflectMethodPrefix.GET_PREFIX)) {
                try {

                    Object result = m.invoke(src);
                    // 浅拷贝忽略非基本类型的属性
                    if (result instanceof BaseEntity || result instanceof List || result == null) {
                        continue;
                    }
                    // get方法的后缀
                    String postName = srcName.substring(3, srcName.length());
                    // 设置方法名
                    String mySetName = ReflectMethodPrefix.SET_PREFIX + postName;

                    Method myMethod = targetSetMethods.get(mySetName);
                    // 有对应的set
                    if (myMethod != null) {
                        // 将获取到的属性值设置给目标对象
                        myMethod.invoke(target, result);
                    }
                } catch (Exception e) {
                }
            }
        }
        return target;
    }

    /**
     * 将src资源中的属性全量复制到target实例里面包含复杂属性,需要注意同名问题
     *
     * @param src    被拷贝对象
     * @param target 保存对象
     * @return Object 拷贝完成后的对象
     * @author W
     * @date: 2021/4/16
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Object populateDeep(Object src, Object target) {
        Method[] srcMethods = src.getClass().getMethods();
        Method[] targetMethods = target.getClass().getMethods();
        // 设置方法映射表
        HashMap<String, Method> targetSetMethods = ReflectionUtils.getMethodMap4Set(targetMethods);
        // 遍历源对象的所有get方法
        for (Method m : srcMethods) {
            String srcName = m.getName();
            // 获取get方法进行设值处理
            if (srcName.startsWith("get")) {
                try {
                    // get方法的后缀
                    String postName = srcName.substring(3, srcName.length());
                    // 设置方法名
                    String mySetName = "set" + postName;
                    Method myMethod = targetSetMethods.get(mySetName);
                    // 有对应的set方法
                    if (myMethod != null) {
                        Object result = m.invoke(src);
                        // 不干任何事情
                        if (result == null) {
                            continue;
                        }
                        // 将dto属性拷贝为对应实体
                        if (result instanceof BaseDTO) {
                            Object myObject = ((BaseDTO) result).dto2EntityDeep();
                            // 将获取到的属性值设置给目标对象
                            myMethod.invoke(target, myObject);
                        }
                        // 将list属性拷贝为set属性
                        if (result instanceof List) {
                            List myList = (List) result;
                            Set mySet = new HashSet(myList.size());

                            for (Object obj : myList) {
                                Object myObject = ((BaseDTO) obj).dto2EntityDeep();
                                mySet.add(myObject);
                            }
                            // 将获取到的属性值设置给目标对象
                            myMethod.invoke(target, mySet);
                        } else {
                            // 将获取到的属性值设置给目标对象
                            myMethod.invoke(target, result);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        return target;
    }

    /**
     * 将所有的DTO集合转换成entity的集合,不能转换复杂属性
     *
     * @param dtos 所有的实体类集合
     * @return List<D> 泛型对应的DTO类型对象集合 List<D>
     * @author W
     * @date: 2021/4/16
     */
    public List<E> dto2Entities(List<?> dtos) {
        if (dtos != null && dtos.size() > 0) {
            List<E> entities = new ArrayList<E>(dtos.size());
            try {
                for (Object dto : dtos) {
                    // 创建实体实例
                    E myEntity = entityClass.newInstance();
                    entities.add(myEntity);
                    populate(dto, myEntity);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("dto2Entities has exception", e);
            }
            return entities;
        }
        return new ArrayList<E>();
    }

    /**
     * 将所有的DTO集合转换成entity的集合,可以转换复杂属性
     *
     * @param dtos 所有的实体类集合
     * @return List<D> 泛型对应的DTO类型对象集合 List<D>
     * @author W
     * @date: 2021/4/16
     */
    public List<E> dto2EntitiesDeep(List<?> dtos) {
        if (dtos != null && dtos.size() > 0) {
            List<E> entities = new ArrayList<E>(dtos.size());
            try {
                for (Object dto : dtos) {
                    // 创建实体实例
                    E myEntity = entityClass.newInstance();
                    entities.add(myEntity);
                    populateDeep(dto, myEntity);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("dto2EntitiesDeep has exception", e);
            }
            return entities;
        }
        return new ArrayList<E>();
    }

    /**
     * 将DTO转换成entity
     *
     * @return E entity的实例
     * @author W
     * @date: 2021/4/16
     */
    public E dto2Entity() {
        try {
            // 创建实体实例
            E myEntity = entityClass.newInstance();
            populate(this, myEntity);
            return myEntity;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("dto2Entity has exception", e);
        }
        return null;
    }

    /**
     * 将DTO转换成entity，并可以拷贝复杂属性
     *
     * @return E entity的实例
     * @author W
     * @date: 2021/4/16
     */
    public E dto2EntityDeep() {
        try {
            // 创建实体实例
            E myEntity = entityClass.newInstance();
            populateDeep(this, myEntity);
            return myEntity;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("dto2EntityDeep has exception", e);
        }

        return null;
    }

}
