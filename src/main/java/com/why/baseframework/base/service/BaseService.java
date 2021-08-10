package com.why.baseframework.base.service;

import com.why.baseframework.base.entity.BaseDocument;
import com.why.baseframework.base.message.I18nMessage;
import com.why.baseframework.base.web.exception.BusinessException;
import com.why.baseframework.enums.ErrCodeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Author y
 * @Description
 * @Date 2021/4/15 20:50
 **/
@SuppressWarnings("rawtypes")
public class BaseService<R extends MongoRepository<T, Serializable>, T extends BaseDocument> {
    /**
     * 国际化语言
     */
    @Autowired
    private I18nMessage i18nMessage;

    @Autowired
    private R baseRepository;

    /**
     * 获取国际化信息
     *
     * @return BaseMessage
     * @author chenglin.wu
     * @date: 2021/4/19
     */
    protected I18nMessage getI18nMessage() {
        return i18nMessage;
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public void removeById(Serializable id){
        this.baseRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public void removeByIds(Serializable[] ids){
        for (Serializable id : ids) {
            this.baseRepository.deleteById(id);
        }
    }

    public List<T> find(T example){
        return this.baseRepository.findAll(Example.of(example));
    }

    public List<T> findAll(){
        return this.baseRepository.findAll();
    }

    public Page<T> findPage(T example, Pageable pageable){
        return this.baseRepository.findAll(Example.of(example), pageable);
    }

    public Page<T> findPage(T example, ExampleMatcher matcher, Pageable pageable){
        return this.baseRepository.findAll(Example.of(example, matcher), pageable);
    }

    public T get(Serializable id) throws Exception {
        return this.baseRepository.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public T insert(T entity){
        entity.setId(null);
        return this.baseRepository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public T update(T entity) throws BusinessException {
        String id = entity.getId();
        if (StringUtils.isBlank(id)){
            throw new BusinessException(ErrCodeEnum.THROWABLE.getCode(),"entity id is empty please check");
        }
        return this.baseRepository.save(entity);
    }

    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public List<T> saveAll(List<T> entityList){
        return this.baseRepository.saveAll(entityList);
    }

    /**
     * @return the baseRepository
     */
    protected R getBaseRepository() {
        return baseRepository;
    }

}
