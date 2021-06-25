package com.why.baseframework.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author W
 * @description:
 * @title: StuSubDto
 * @projectName tianrui
 * @date 2021年05月30日
 * @company WHY-Group
 */
@Data
@ApiModel("复杂查询的分页dto")
public class ComplexPageDto<T, D> {
    @ApiModelProperty(value = "分页的dto")
    @NotNull(message = "分页查询的dto不能为空")
    private PageDto<T> pageDto;

    @ApiModelProperty(value = "复杂查询条件")
    private D complexData;
}
