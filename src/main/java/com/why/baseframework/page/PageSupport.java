package com.why.baseframework.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.why.baseframework.base.dto.PageDto;

/**
 * @Author Y
 * @Description:
 * @Title: PageSupport
 * @ProjectName oracle
 * @Date 2021/5/11
 * @Company WHY-Group
 */
public final class PageSupport {

    /**
     * 从servlet获取pageNum与pageSize 创建page对象
     *
     * @Return {@link Page<T> }
     * @Author Y
     * @Date 2021/5/11 13:36
     **/
    public static <T> Page<T> createPage(PageDto<T> pageDto) {
        return PageForm.createPage(pageDto.getPage().getCurrent(),pageDto.getPage().getSize());
    }
}
