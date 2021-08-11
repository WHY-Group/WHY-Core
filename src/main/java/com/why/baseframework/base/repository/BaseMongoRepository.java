package com.why.baseframework.base.repository;


import com.why.baseframework.base.entity.BaseDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * @Author Y
 * @Description: 基础mapper
 * @Title: BaseMonfoRepository
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company  WHY-Group
 **/
@SuppressWarnings("rawtypes")
@NoRepositoryBean
public interface BaseMongoRepository<T extends BaseDocument> extends MongoRepository<T, Serializable> {
}
