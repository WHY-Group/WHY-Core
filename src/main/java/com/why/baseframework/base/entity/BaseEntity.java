package com.why.baseframework.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.why.baseframework.base.dto.BaseDTO;
import com.why.baseframework.base.web.jsonserial.CustomDateSerializer;
import com.why.baseframework.base.web.jsonserial.CustomTimeSerializer;
import com.why.baseframework.base.web.jsonserial.DateDeserializer;
import com.why.baseframework.util.ReflectionUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author W
 * @Description:
 * @Title: BaseEntity
 * @ProjectName WHY-Core
 * @Date 2021/4/16 10:20
 * @Company  WHY-Group
 */
@Slf4j
@Data
public abstract class BaseEntity<D extends BaseDTO<?>> implements Serializable {
    /**
     * 序列化version
     */
    private static final long serialVersionUID = -7768930415492409034L;
    /**
     * 对应dto的类型
     */
    @TableField(exist = false)
    protected Class<D> dtoClass;
    /**
     * entity基类的构造函数，重点是设置entity对应dto的类型
     */
    @SuppressWarnings("unchecked")
    public BaseEntity() {
        this.dtoClass = ReflectionUtils.getSuperClassGenericType(getClass());
    }

    /**
     * 将Entity转换成DTO时只转换基本类型或其包装类，加String类型不转换复杂的类型,需要注意同名问题
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
            if (srcName.startsWith("get")) {
                try {
                    Object result = m.invoke(src);
                    // 浅拷贝，忽略非基本类型的属性
                    if (result instanceof BaseEntity || result instanceof Set || result instanceof List
                            || result == null) {
                        continue;
                    }
                    // get方法的后缀
                    String postName = srcName.substring(3, srcName.length());
                    // 设置方法名
                    String mySetName = "set" + postName;
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
                        // 如果为空，不干任何事情
                        if (result == null) {
                            continue;
                        }
                        // 将dto属性拷贝为对应实体
                        if (result instanceof BaseEntity) {
                            Object myObject = ((BaseEntity) result).entity2DtoDeep();
                            // 将获取到的属性值设置给目标对象
                            myMethod.invoke(target, myObject);
                        }
                        // 将list属性拷贝为set属性
                        if (result instanceof Collection) {
                            Collection collection = (Collection) result;
                            List list = new ArrayList(collection.size());

                            collection.forEach(obj -> {
                                if (obj instanceof BaseEntity) {
                                    BaseDTO dto = ((BaseEntity) obj).entity2DtoDeep();
                                    list.add(dto);
                                } else {
                                    list.add(obj);
                                }
                            });
                            // 将获取到的属性值设置给目标对象
                            myMethod.invoke(target, list);
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
     * 将所有的实体类集合转换成DTO的集合,不能转换复杂属性
     *
     * @param entities 所有的实体类集合
     * @return List<D> 泛型对应的DTO类型对象集合 List<D>
     * @author W
     * @date: 2021/4/16
     */
    public List<D> entities2Dtos(List<?> entities) {
        if (entities != null && entities.size() > 0) {
            List<D> dtos = new ArrayList<D>(entities.size());
            try {
                for (Object dto : entities) {
                    // 创建实体实例
                    D myDto = dtoClass.newInstance();
                    dtos.add(myDto);
                    populate(dto, myDto);
                }

            } catch (InstantiationException e) {
                log.error("InstantiationException", e);
            } catch (IllegalAccessException e) {
                log.error("IllegalAccessException", e);
            }

            return dtos;
        }

        return new ArrayList<D>();
    }

    /**
     * 将所有的实体类集合转换成DTO的集合,可以转换复杂属性
     *
     * @param entities 所有的实体类集合
     * @return List<D> 泛型对应的DTO类型对象集合 List<D>
     * @author W
     * @date: 2021/4/16
     */
    public List<D> entities2DtosDeep(List<?> entities) {
        if (entities != null && entities.size() > 0) {
            List<D> dtos = new ArrayList<D>(entities.size());
            try {
                for (Object entity : entities) {
                    // 创建实体实例
                    D myDto = dtoClass.newInstance();
                    dtos.add(myDto);
                    populateDeep(entity, myDto);
                }

            } catch (InstantiationException | IllegalAccessException e) {
                log.error("entities2DtosDeep has exception", e);
            }

            return dtos;
        }

        return new ArrayList<D>();
    }

    /**
     * 将entity转换成DTO
     *
     * @return D DTO的实例
     * @author W
     * @date: 2021/4/16
     */
    public D entity2Dto() {
        try {
            // 创建实体实例
            D myDto = dtoClass.newInstance();

            populate(this, myDto);
            return myDto;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("entity2Dto has exception", e);
        }

        return null;
    }

    /**
     * 将实体类转换成dto类，并可以拷贝复杂属性
     *
     * @return D DTO的实例
     * @author W
     * @date: 2021/4/16
     */
    public D entity2DtoDeep() {
        try {
            // 创建实体实例
            D myDto = dtoClass.newInstance();

            populateDeep(this, myDto);
            return myDto;
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("entity2DtoDeep has exception", e);
        }

        return null;
    }


}
