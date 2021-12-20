package com.why.baseframework.base.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.why.baseframework.base.web.jsonserial.CustomTimeSerializer;
import com.why.baseframework.base.web.jsonserial.DateDeserializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author W
 * @description:
 * @title: PageExtraI
 * @projectName WHY-Core
 * @date 2021年05月23日
 * @company WHY-Group
 */
@Data
@ApiModel("分页查询的扩展字段，时间扩展")
public class PageExtra implements Serializable {
    private static final long serialVersionUID = 8809935877704099479L;
    /**
     * 时间的列名
     */
    @ApiModelProperty("数据库列名，必须与数据库的列名一一对应")
    private String columnName;

    /**
     * 开始时间
     */
    @JsonSerialize(using = CustomTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @ApiModelProperty("开始时间")
    private Date beginTime;
    /**
     * 结束时间
     */
    @JsonSerialize(using = CustomTimeSerializer.class)
    @JsonDeserialize(using = DateDeserializer.class)
    @ApiModelProperty("结束时间")
    private Date endTime;
}
