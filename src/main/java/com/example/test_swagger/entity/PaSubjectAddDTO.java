package com.example.test_swagger.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shaoqk
 * @create 2020-12-16 10:55
 */
@Data
@ApiModel("题目表新增入参")
public class PaSubjectAddDTO {

    @ApiModelProperty(value = "题目内容")
    private String subjectContent;

    @ApiModelProperty(value = "正确答案编码")
    private String answerCode;

    @ApiModelProperty(value = "所有答案内容")
    private String[] answerContent;

    @ApiModelProperty(value = "正确答案内容")
    private String answerRight;

}