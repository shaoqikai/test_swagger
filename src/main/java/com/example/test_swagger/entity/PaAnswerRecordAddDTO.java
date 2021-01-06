package com.example.test_swagger.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author shaoqk
 * @create 2020-12-19 11:18
 */
@Data
public class PaAnswerRecordAddDTO {

    @NotBlank(message = "user_id不可为空")
    @ApiModelProperty(value = "答题人id")
    private String userId;

    @ApiModelProperty(value = "答题人姓名")
    private String userName;

    @ApiModelProperty(value = "选中答案编码")
    private String answerContentChecked;

    @ApiModelProperty(value = "正确答案编码")
    private String answerContent;

    @ApiModelProperty(value = "选项内容")
    private List<PaAnswer> answers;

    @ApiModelProperty(value = "是否答完")
    private String answerStatus;

    @ApiModelProperty(value = "是否答对")
    private Integer answerYes;

    @ApiModelProperty(value = "所在街道")
    private String address;




}
