package com.why.baseframework.util;

import com.why.baseframework.base.entity.BaseDocument;
import com.why.baseframework.constants.ReflectConstants;
import com.why.baseframework.constants.WrapperConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author chenglin.wu
 * @description:
 * @title: QueryUtils
 * @projectName WHY-Core
 * @date 2021/8/10
 * @company 上海织田信息技术有限公司 (Oda-Group)
 */
public class QueryUtils {
    private static final Logger log = LoggerFactory.getLogger(ReflectionUtils.class);

    private static final String ID = "id";

    private QueryUtils(){}


    /**
     * 通过当前实体类的数据获取查询的 query对象，所有的String类型都为模糊匹配
     * 所有的基本数据类型包括其包装类都为精准匹配;
     * 并且当前所有属性的关系都为and关系
     *
     * @param entity 守实体类
     * @return QueryWrapper<T>
     * @throws NoSuchMethodException,InvocationTargetException,IllegalAccessException
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    public static <T> Query createLikeQuery(T entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Query query = new Query();
        Criteria criteria = new Criteria();

        DeclaredAndSuperClass declaredAndSuperClass = ReflectionUtils.getDeclaredAndSuperClass(entity);
        List<Field> allFields = declaredAndSuperClass.getAllFields();
        // 遍历属性
        for (Field field : allFields) {
            // 属性类型
            String fieldType = field.getType().getSimpleName();
            boolean ignoreField = ReflectionUtils.judgementIgnoreField(field);
            if (ReflectionUtils.DATA_TYPE_LIST.contains(fieldType) && !ignoreField) {
                String getMethodName = ReflectConstants.GET_PREFIX + StrChartUtil.upperCaseFirst(field.getName());
                try {
                    Method method = declaredAndSuperClass.getDeclaredClass().getMethod(getMethodName);
                    getCriteria(entity, field, method, criteria);
                } catch (NoSuchMethodException e) {
                    Method method = declaredAndSuperClass.getSuperClass().getMethod(getMethodName);
                    getCriteria(entity, field, method, criteria);
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
        return query.addCriteria(criteria);
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
//    private static <T extends BaseDocument> void getExtendField(T entity, Class<?> aClass, QueryWrapper<T> wrapper) throws Exception {
//        Method method = aClass.getMethod(ReflectConstants.GET_PARAMS);
//        Object invoke = null;
//        try {
//            invoke = method.invoke(entity);
//        } catch (IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
//        if (ObjectUtils.allNotNull(invoke)) {
//            Map<String, Object> betweenMap = (Map<String, Object>) invoke;
//            //设置筛选时间
//            setBetweenMap(betweenMap);
//            //设置筛选时间字段
//            wrapper.between(WrapperConstants.CREATE_TIME,
//                    betweenMap.get(WrapperConstants.BEGIN_TIME),
//                    betweenMap.get(WrapperConstants.END_TIME));
//        }
//    }


    /**
     * 构造Criteria
     *
     * @param entity  实体类
     * @param field   属性
     * @param method  get 方法
     * @param criteria 查询mongo具体sql条件
     * @return void
     * @author chenglin.wu
     * @date: 2021/5/17
     */
    private static <T> void getCriteria(T entity, Field field, Method method, Criteria criteria) throws IllegalAccessException, InvocationTargetException {
        Object invoke = method.invoke(entity);
        if (ObjectUtils.anyNull(invoke)) {
            return;
        }
        String columnName = ReflectionUtils.getTranslationWithSeparatorFieldName(field);

        // 构造查询条件
        if (ReflectConstants.STRING_SIMPLE_NAME.equals(field.getType().getSimpleName()) && ObjectUtils.isNotEmpty(invoke)) {
            if (ID.equals(columnName)){
                criteria.and(columnName).is(invoke);
            }else {
                criteria.and(columnName).regex(".*" + invoke + ".*");
            }
        } else if (ObjectUtils.isNotEmpty(invoke)) {
            criteria.and(columnName).is(invoke);
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
}
