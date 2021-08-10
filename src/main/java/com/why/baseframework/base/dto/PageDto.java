package com.why.baseframework.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author chenglin.wu
 * @description:
 * @title: PageDto
 * @projectName tianrui
 * @date 2021年05月23日
 * @company WHY-Group
 */
@Data
@ApiModel("分页查询的查询DTO泛型为数据库的entity")
public class PageDto<T> {
    /**
     * 系统自带的分页对象
     */
    @NotNull(message = "查询的page对象不能为空")
    @ApiModelProperty(value = "查询的page对象", required = true)
    private Page<T> page;
    /**
     * 查询模板
     */
    @ApiModelProperty(value = "用于like查询的数据")
    private T searchExample;
    /**
     * 扩展字段
     */
    @ApiModelProperty(value = "扩展字段,主要用于时间类型的分段查询进行扩展")
    private List<PageExtra> extras;
}
