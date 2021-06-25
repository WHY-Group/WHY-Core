package com.why.baseframework.base.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.why.baseframework.base.entity.BaseEntity;

/**
 * @Author Y
 * @Description: 基础mapper
 * @Title: BaseCustomMapper
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company  WHY-Group
 **/
@SuppressWarnings("rawtypes")
public interface BaseCustomMapper<T extends BaseEntity> extends BaseMapper<T> {

}
