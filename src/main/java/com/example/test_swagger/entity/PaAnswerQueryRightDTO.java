package com.example.test_swagger.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shaoqk
 * @create 2020-12-29 18:23
 */
@Data
@NoArgsConstructor
public class PaAnswerQueryRightDTO {
    @ApiModelProperty(value = "答对题目数")
    private Integer answerYes;

    @ApiModelProperty(value = "答错题目数")
    private Integer answerNO;
}
