package com.why.baseframework.base.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.why.baseframework.base.dto.PageInfo;
import com.why.baseframework.base.dto.PageSearch;
import com.why.baseframework.base.entity.BaseDocument;
import com.why.baseframework.base.message.I18nMessage;
import com.why.baseframework.base.web.exception.BusinessException;
import com.why.baseframework.enums.ErrCodeEnum;
import com.why.baseframework.util.QueryUtils;
import com.why.baseframework.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @Author y
 * @Description
 * @Date 2021/4/15 20:50
 **/
@SuppressWarnings("rawtypes")
public class BaseMongoService<R extends MongoRepository<T, Serializable>, T extends BaseDocument> {

    @JsonIgnore
    private Class<T> docClass;
    /**
     * 国际化语言
     */
    @Autowired
    private I18nMessage i18nMessage;

    @Autowired
    private R baseRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public BaseMongoService() {
        this.docClass = ReflectionUtils.getSuperClassGenericType(getClass(),1);
    }

    public Class<T> getDocClass() {
        return docClass;
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

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
     * 分页查询 主要解决有时间查询的字段，用between的方式解决，所有的条件都是and关系
     *
     * @param pageSearch 分页查询的search类
     * @return Page<T>
     * @author chenglin.wu
     * @date: 2021/8/10
     */
    public PageInfo<T> findPage(PageSearch<T> pageSearch) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        PageInfo<T> page = pageSearch.getPage();

        // 创建查询的基本语句
        Query query = QueryUtils.createLikeQuery(pageSearch.getSearchExample(), true, pageSearch.getSort());
        // 创建扩展语句，主要用于时间的扩展
        QueryUtils.queryWithExtra(query, pageSearch.getExtras());

        // count总条数
        long count = mongoTemplate.count(query, this.getDocClass());
        long totalPage = count / page.getPageSize();
        long mod = count % page.getPageSize();
        if (mod != 0) {
            totalPage += 1;
        }
        page.setTotalPage(totalPage);
        // 构造skip和limit实现真正的分页
        QueryUtils.pagingQuery(pageSearch, query);
        // 查询数据并返回
        List<T> content = mongoTemplate.find(query, this.getDocClass());
        page.setContent(content);
        return page;
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
