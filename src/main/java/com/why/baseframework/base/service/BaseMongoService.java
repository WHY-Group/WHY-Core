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
public class BaseMongoService<R extends MongoRepository<T, Serializable>, T extends BaseDocument> {
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

    /**
     * 通过id删除一条记录
     *
     * @param id the id
     * @return void
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public void removeById(Serializable id) {
        this.baseRepository.deleteById(id);
    }

    /**
     * 通过id的集合删除一些数据
     *
     * @param ids the ids id的数组
     * @return void
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public void removeByIds(Serializable[] ids) {
        for (Serializable id : ids) {
            this.baseRepository.deleteById(id);
        }
    }

    /**
     * 通过id查找一条记录
     *
     * @param id the id
     * @return T
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    public T findById(Serializable id) {
        return this.baseRepository.findById(id).orElse(null);
    }

    /**
     * 通过查询模板进行查询
     *
     * @param example the example
     * @return List<T>
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    public List<T> find(T example) {
        return this.baseRepository.findAll(Example.of(example));
    }

    /**
     * 查询所有
     *
     * @return List<T>
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    public List<T> findAll() {
        return this.baseRepository.findAll();
    }

    /**
     * 分页查询
     *
     * @param example  查询模板
     * @param pageable 分页的条件
     * @return Page<T>
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    public Page<T> findPage(T example, Pageable pageable) {
        return this.baseRepository.findAll(Example.of(example), pageable);
    }

    /**
     * 分页查询
     *
     * @param example  查询模板
     * @param matcher  匹配数据
     * @param pageable 分页条件
     * @return Page<T>
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    public Page<T> findPage(T example, ExampleMatcher matcher, Pageable pageable) {
        return this.baseRepository.findAll(Example.of(example, matcher), pageable);
    }

    /**
     * 插入一条数据
     *
     * @param entity 实体类对象
     * @return T
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public T insert(T entity) {
        entity.setId(null);
        return this.baseRepository.save(entity);
    }

    /**
     * 更新一条数据
     *
     * @param entity 更新数据的实体
     * @return T
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public T update(T entity) throws BusinessException {
        String id = entity.getId();
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(ErrCodeEnum.THROWABLE.getCode(), "entity id is empty please check");
        }
        return this.baseRepository.save(entity);
    }

    /**
     * 保存或者更新所有
     *
     * @param entityList 实体类的集合
     * @return List<T>
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    @Transactional(rollbackFor = Exception.class, transactionManager = "mongoTransactionManager")
    public List<T> saveAll(List<T> entityList) {
        return this.baseRepository.saveAll(entityList);
    }

    /**
     * @return the baseRepository
     */
    protected R getBaseRepository() {
        return baseRepository;
    }

}
