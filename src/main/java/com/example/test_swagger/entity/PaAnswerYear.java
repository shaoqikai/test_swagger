package com.example.test_swagger.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shaoqk
 * @create 2020-12-30 11:40
 */
@Data
public class PaAnswerYear {

    @ApiModelProperty(value = "筛选类型  0 - 本月  1 - 本年")
    private Integer screenYear;

}
