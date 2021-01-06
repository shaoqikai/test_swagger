package com.example.test_swagger.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shaoqk
 * @create 2020-12-30 11:45
 */
@Data
public class PaAnswerRecordDTO {

    @ApiModelProperty(value = "地区")
    private String address;

    @ApiModelProperty(value = "排名")
    private Integer raking;

    @ApiModelProperty(value = "总竞答数量")
    private Integer allNumber;

    @ApiModelProperty(value = "时间")
    private String deteLine;

    @ApiModelProperty(value = "环比率")
    private String  ringRatio;

    @ApiModelProperty(value = "本期数")
    private String toDay;

    @ApiModelProperty(value = "上期数")
    private String yesterDay;

    @ApiModelProperty(value = "本期数")
    private Integer toDayCount;

    @ApiModelProperty(value = "上期数")
    private Integer yesterDayCount;

}
