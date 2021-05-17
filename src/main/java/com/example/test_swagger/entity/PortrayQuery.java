package com.example.test_swagger.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author shaoqk
 * @create 2021-03-17 17:15
 */
@Data
//@AllArgsConstructor
public class PortrayQuery {

    @ApiModelProperty(value = "查询时间起")
    private String dateStart;

    @ApiModelProperty(value = "查询时间止")
    private String dateEnd;

    @ApiModelProperty("值")
    private Integer count;

    @ApiModelProperty("键")
    private String type;
}
