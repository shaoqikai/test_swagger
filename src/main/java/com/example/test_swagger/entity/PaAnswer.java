package com.example.test_swagger.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shaoqk
 * @create 2020-12-16 13:36
 */
@Data
@NoArgsConstructor
public class PaAnswer {
    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 选项编码  A  B  C  D  E  F
     */
    private String answerCode;
    /**
     * 选项内容
     */
    private String answerContent;

    /**
     * 题目表ID
     */
    private Integer subjectId;

    /**
     * 项编码  A  B  C  D  E  F选
     */
    private Integer answerRight;

    /**
     * 答题积分
     */
    private Integer points;


    /**
     * 所在街道
     */
    private String address;

}