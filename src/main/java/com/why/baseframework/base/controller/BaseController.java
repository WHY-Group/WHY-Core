package com.why.baseframework.base.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.why.baseframework.base.dto.DefaultOrderItem;
import com.why.baseframework.base.dto.PageDto;
import com.why.baseframework.base.dto.PageExtra;
import com.why.baseframework.base.entity.BaseEntity;
import com.why.baseframework.base.mapper.BaseCustomMapper;
import com.why.baseframework.base.message.BaseMessage;
import com.why.baseframework.base.service.BaseService;
import com.why.baseframework.base.web.response.ResponseResult;
import com.why.baseframework.base.web.response.ResponseUtils;
import com.why.baseframework.dto.LoginUser;
import com.why.baseframework.enums.ErrCodeEnum;
import com.why.baseframework.page.PageSupport;
import com.why.baseframework.redis.RedisLoginUserManager;
import com.why.baseframework.util.ReflectionUtils;
import com.why.baseframework.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author Y
 * @Description: 基础controller
 * @Title: BaseController
 * @ProjectName base_framework
 * @Date 2021/4/16 15:20
 * @Company WHY-Group
 **/
@Slf4j
@SuppressWarnings({"rawtypes", "SpringJavaAutowiredMembersInspection"})
public class BaseController<S extends BaseService<M, T>, M extends BaseCustomMapper<T>, T extends BaseEntity> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * request
     **/
    @Autowired
    private HttpServletRequest request;

    /**
     * 管理用户redis
     **/
    @Autowired
    private RedisLoginUserManager redisManager;

    /**
     * 国际化语言
     **/
    @Autowired
    private BaseMessage baseMessage;

    /**
     * 业务处理类
     */
    @Autowired
    protected S baseService;

    /**
     * 添加用户到redis中，请在使用时，对loginUser的token与userId进行后缀的添加
     *
     * @param loginUser
     * @Author Y
     * @Date 2021/4/16 16:09
     **/
    public void setLoginUserToRedis(LoginUser loginUser) {
        this.redisManager.setLoginUser(loginUser);
    }

    /**
     * 获取redis中的LoginUser
     *
     * @Return {@link LoginUser }
     * @Author Y
     * @Description
     * @Date 2021/4/16 16:09
     **/
    public LoginUser getLoginUserFromRedis() {
        // 获取登录对象token
        String token = TokenUtil.getTokenFromServlet(this.request);
        // 返回登录对象
        return this.redisManager.getLoginUserByToken(this.getToken());
    }

    /**
     * 通过token去删除redis中的user
     *
     * @param token
     * @Author Y
     * @Date 2021/4/15 21:36
     **/
    public void deleteLoginUserFromRedis(String token) {
        this.redisManager.deleteLoginUser(token);
    }

    /**
     * 更新redis中的LoginUser
     *
     * @param loginUser
     * @Author Y
     * @Date 2021/4/16 16:11
     **/
    public void updateLoginUserToRedis(LoginUser loginUser) {
        this.redisManager.delayLoginUserTime(loginUser);
    }

    /**
     * 获取当前请求的httpServlet对象
     *
     * @return: HttpServletRequest
     * @Author: H
     * @Date: 2021/4/16
     */
    public HttpServletRequest getHttpServletRequest() {
        return this.request;
    }

    /**
     * 获取redisTemplate的对象
     *
     * @return: RedisTemplate
     * @Author: H
     * @Date: 2021/4/16
     */
    public RedisTemplate<String, Object> getRedisTemplate() {
        return this.redisTemplate;
    }

    /**
     * 获取业务处理层的实例
     *
     * @return S 业务处理层的类
     * @author W
     * @date: 2021/4/16
     */
    public S getBaseService() {
        return baseService;
    }

    /**
     * 获取国际化语言的实例
     *
     * @return BaseMessage 国际化语言的实例类
     * @author W
     * @date: 2021/4/16
     */
    public BaseMessage getBaseMessage() {
        return baseMessage;
    }

    /**
     * 获取redisLoginUserManager
     *
     * @return RedisLoginUserManager 登录对象操作类
     * @author W
     * @date: 2021/4/16
     */
    public RedisLoginUserManager getRedisManager() {
        return redisManager;
    }

    /**
     * 获取header中的token
     *
     * @return String 请求中的token
     * @author W
     * @date: 2021/4/16
     */
    public String getToken() {
        return TokenUtil.getTokenFromServlet(this.request);
    }

    /**
     * 新增实体数据
     *
     * @return ResponseResult<T>
     * @param: entity
     * @author W
     * @date 2021-05-25
     */
    public ResponseResult<T> save(T entity) {
        try {
            Class<? extends BaseEntity> aClass = entity.getClass();
            Method setId = aClass.getDeclaredMethod("setId");
            setId.invoke(entity, (Object) null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.info("set id null error");
        }
        this.getBaseService().save(entity);
        return ResponseUtils.success(entity);
    }

    /**
     * 通过ID删除数据
     *
     * @return ResponseResult<String>
     * @param: id 实体类的id
     * @author W
     * @date 2021-05-25
     */
    public ResponseResult<String> delete(String id) {
        this.getBaseService().removeById(id);
        return ResponseUtils.success();
    }

    /**
     * 通过ID删除数据
     *
     * @return ResponseResult<String>
     * @param: id 实体类的id
     * @author W
     * @date 2021-05-25
     */
    public ResponseResult<String> deleteListId(List<String> id) {
        this.getBaseService().removeByIds(id);
        return ResponseUtils.success();
    }

    /**
     * 更新一条数据
     *
     * @return ResponseResult<T>
     * @param: entity 实体类数据
     * @author W
     * @date 2021-05-25
     */
    public ResponseResult<T> update(T entity) {
        boolean flag = this.getBaseService().saveOrUpdate(entity);
        if (flag) {
            return ResponseUtils.success();
        }
        return ResponseUtils.fail(ErrCodeEnum.DATA_CHECK.getCode(), "更新失败");
    }

    /**
     * 分页查询的数据
     *
     * @return ResponseResult<Page < T>>
     * @param: pageDto 分页的dto
     * @author W
     * @date 2021-05-24
     */
    public ResponseResult<Page<T>> findPage(PageDto<T> pageDto) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        QueryWrapper<T> queryWrapper = this.getPageQueryWrapper(pageDto);
        // 如果没有传排序字段和排序方式则使用create_time默认排序
        Page<T> inputPage = this.getOrder(pageDto);
        Page<T> page = this.getBaseService().page(inputPage, queryWrapper);
        return ResponseUtils.success(page);
    }

    /**
     * 获取分页的查询wrapper
     *
     * @return QueryWrapper<T>
     * @param: pageDto
     * @author W
     * @date 2021-05-29
     */
    private QueryWrapper<T> getPageQueryWrapper(PageDto<T> pageDto) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        if (ObjectUtils.allNotNull(pageDto.getSearchExample())) {
            ReflectionUtils.createPageWrapper2Field(pageDto.getSearchExample(), queryWrapper);
        }
        // 扩展字段
        if (ObjectUtils.isNotEmpty(pageDto.getExtras())) {
            for (PageExtra extra : pageDto.getExtras()) {
                if (ObjectUtils.anyNotNull(extra.getBeginTime(), extra.getEndTime())) {
                    queryWrapper.between(extra.getColumnName(), extra.getBeginTime(), extra.getEndTime());
                } else if (ObjectUtils.isEmpty(extra.getBeginTime()) && ObjectUtils.isNotEmpty(extra.getEndTime())) {
                    queryWrapper.le(extra.getColumnName(), extra.getEndTime());
                } else if (ObjectUtils.isNotEmpty(extra.getBeginTime()) && ObjectUtils.isEmpty(extra.getEndTime())) {
                    queryWrapper.ge(extra.getColumnName(), extra.getBeginTime());
                }
            }
        }
        return queryWrapper;
    }

    /**
     * 分页查询，排除自己
     *
     * @return ResponseResult<Page < T>>
     * @param: pageDto 分页的dto
     * @param: myselfId 当前登录用户的id
     * @author W
     * @date 2021-05-29
     */
    public ResponseResult<Page<T>> findPage(PageDto<T> pageDto, String myselfId) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        QueryWrapper<T> queryWrapper = this.getPageQueryWrapper(pageDto);
        Page<T> inputPage = this.getOrder(pageDto);
        queryWrapper.ne("id", myselfId);

        Page<T> page = this.getBaseService().page(inputPage, queryWrapper);
        return ResponseUtils.success(page);
    }

    /**
     * 获取分页排序的数据
     *
     * @return Page<T>
     * @param: pageDto 分页的dto
     * @author W
     * @date 2021-05-29
     */
    private Page<T> getOrder(PageDto<T> pageDto) {
        // 如果没有传排序字段和排序方式则使用create_time默认排序
        Page<T> inputPage = pageDto.getPage();
        List<OrderItem> orders = inputPage.getOrders();
        if (orders.isEmpty()) {
            inputPage.addOrder(DefaultOrderItem.getDefault());
        }
        return inputPage;
    }

    /**
     * 创建分页对象,预留为mapper.xml使用
     *
     * @Return {@link Page<T> }
     * @Author Y
     * @Date 2021/5/11 10:56
     **/
    public Page<T> createPage(PageDto<T> pageDto) {
        return PageSupport.createPage(pageDto);
    }

    /**
     * 创建不是当前T的分页对象，可以為任何class
     *
     * @Return {@link Page <T> }
     * @Author Y
     * @Date 2021/5/11 10:56
     **/
    public <D> Page<D> createPageBy(PageDto<D> pageDto) {
        return PageSupport.createPage(pageDto);
    }

}
