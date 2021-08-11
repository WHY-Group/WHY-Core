package com.why.baseframework.base.dto;

import com.why.baseframework.constants.IntConstants;
import lombok.Data;

import java.util.List;

@Data
public class PageInfo<T> {

    /**
     * 页数 从0开始为第一页
     */
    private int pageNumber = 0;

    /**
     * 每页记录数，默认20
     */
    private int pageSize = IntConstants.INT_20;

    /**
     * 总页数
     */
    private long totalPage;

    /**
     * 查询出来的数据集合
     */
    private List<T> content;
}

