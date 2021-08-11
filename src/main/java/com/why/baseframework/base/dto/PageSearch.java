package com.why.baseframework.base.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.why.baseframework.constants.QueryConstants;
import com.why.baseframework.util.ReflectionUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author chenglin.wu
 * @description:
 * @title: PageSearch
 * @projectName tianrui
 * @date 2021年05月23日
 * @company WHY-Group
 */
@Data
@ApiModel("分页查询的查询DTO泛型为数据库的entity")
public class PageSearch<T> {

    @JsonIgnore
    private Class<T> searchClass;
    /**
     * 系统自带的分页对象
     */
    @NotNull(message = "查询的page对象不能为空")
    @ApiModelProperty(value = "查询的page对象", required = true)
    private PageInfo<T> page;
    /**
     * 查询模板
     */
    @ApiModelProperty(value = "用于like查询的数据")
    private T searchExample;
    /**
     * 查询的排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private Set<OrderItem> orders;
    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "扩展字段,主要用于时间类型的分段查询进行扩展")
    private List<PageExtra> extras;

    public PageSearch(){
        this.searchClass = ReflectionUtils.getSuperClassGenericType(getClass());
    }
    /**
     * 获取排序的字段
     *
     * @return Sort
     * @author chenglin.wu
     * @date: 2021/8/11
     */
    public Sort getSort() {
        if (ObjectUtils.anyNull(this.orders)) {
            return QueryConstants.DEFAULT_SORT;
        }
        List<Sort.Order> sortOrders = new ArrayList<>();
        for (OrderItem order : this.orders) {
            sortOrders.add(order.getOrder());
        }
        return Sort.by(sortOrders);
    }

}
