package com.why.baseframework.base.dto;

import com.why.baseframework.constants.QueryConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;

/**
 * @author W
 * @description:
 * @title: OrderItem
 * @projectName tianrui
 * @date 2021年05月29日
 * @company WHY-Group
 */
@Data
public class OrderItem {
    /**
     * 排序字段属性
     */
    private String orderColumn;

    /**
     * ASC、DESC
     */
    private String direction;


    /**
     * 获取排序的order
     *
     * @return Order 排序的order
     * @author chenglin.wu
     * @date: 2021/8/11
     */
    public Sort.Order getOrder() {
        if (StringUtils.isBlank(this.direction)) {
            return new Sort.Order(Sort.Direction.ASC, orderColumn);
        } else if (QueryConstants.DESC.equals(direction.toUpperCase())) {
            return Sort.Order.desc(orderColumn);
        }
        return Sort.Order.asc(orderColumn);
    }
}
