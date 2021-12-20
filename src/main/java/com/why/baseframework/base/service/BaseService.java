package com.why.baseframework.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.why.baseframework.base.entity.BaseEntity;
import com.why.baseframework.base.mapper.BaseCustomMapper;
import com.why.baseframework.base.message.BaseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 * @Author y
 * @Description
 * @Date 2021/4/15 20:50
 **/
@SuppressWarnings("rawtypes")
public class BaseService<M extends BaseCustomMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> {
    /**
     * 国际化语言
     */
    @Autowired
    @SuppressWarnings("SpringJavaAutowiredMembersInspection")
    private BaseMessage i18nMessage;

    /**
     * 获取国际化信息
     *
     * @return BaseMessage
     * @author W
     * @date: 2021/4/19
     */
    public BaseMessage getI18nMessage() {
        return i18nMessage;
    }

    /**
     * 数据的insert操作
     *
     * @param entity 实体类的对象
     * @return boolean
     * @author W
     * @date: 2021/4/20
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(T entity) {
        return SqlHelper.retBool(getBaseMapper().insert(entity));
    }

    /**
     * 通过ID删除一条数据
     *
     * @param id id
     * @return boolean
     * @author W
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        return SqlHelper.retBool(this.getBaseMapper().deleteById(id));
    }

    /**
     * 通过map直接删除数据
     *
     * @param columnMap map
     * @return boolean
     * @author W
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByMap(Map<String, Object> columnMap) {
        Assert.notEmpty(columnMap, "error: columnMap must not be empty");
        return SqlHelper.retBool(getBaseMapper().deleteByMap(columnMap));
    }

    /**
     * 通过wrapper删除数据
     *
     * @param queryWrapper wrapper
     * @return boolean
     * @author W
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean remove(Wrapper<T> queryWrapper) {
        return SqlHelper.retBool(getBaseMapper().delete(queryWrapper));
    }

    /**
     * 通过ID批量删除数据
     *
     * @param idList id的集合
     * @return boolean
     * @author W
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }

        return SqlHelper.retBool(getBaseMapper().deleteBatchIds(idList));
    }

    /**
     * 通过实体类更新
     *
     * @param entity 实体类
     * @return boolean
     * @author W
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(T entity) {
        return SqlHelper.retBool(getBaseMapper().updateById(entity));
    }

    /**
     * 通过wrapper直接更新
     *
     * @param updateWrapper wrapper
     * @return boolean
     * @author W
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(Wrapper<T> updateWrapper) {
        return update(null, updateWrapper);
    }

    /**
     * 通过entity和wrapper进行更新操作
     *
     * @param entity        实体类
     * @param updateWrapper 更新的Wrapper
     * @return boolean
     * @author W
     * @date: 2021/4/19
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(T entity, Wrapper<T> updateWrapper) {
        return SqlHelper.retBool(getBaseMapper().update(entity, updateWrapper));
    }
}
