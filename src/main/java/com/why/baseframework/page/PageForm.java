package com.why.baseframework.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @Author Y
 * @Description: 创建page分页对象
 * @Title: PageForm
 * @ProjectName oracle
 * @Date 2021/5/11
 * @Company WHY-Group
 */
public final class PageForm {

    /**
     * 默认当前页
     */
    public static Integer DEFAULT_PAGE_NUMBER = 1;

    /**
     * 默认每页显示条数
     */
    public static Integer DEFAULT_PAGE_SIZE = 10;

    /**
     * 创建{@link Page<T> }对象
     *
     * @Return {@link Page<T> }
     * @Author Y
     * @Date 2021/5/11 9:44
     **/
    public static <T> Page<T> createPage(long pageNum, long pageSize) {
        return new Page<>(
                //当前页
                pageNum > 0 ? pageNum : DEFAULT_PAGE_NUMBER
                ,
                //每页显示条数
                pageSize > 0 ? pageSize : DEFAULT_PAGE_SIZE
        );
    }
}
