package com.example.test_swagger.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author shaoqk
 * @create 2020-12-18 19:59
 */
@Data
public class PaSubjectQueryDTOTwo {

    /**
     * 题目内容
     */
    private String subjectContent;

    /**
     * 答案内容
     */
    private String answerContent;

    /**
     * 答案code
     */
    private String codes;

    /**
     * 正确答案id
     */
    private Integer answerId;

    /**
     * 正确答案编码
     */
    private String answerRight;

    /**
     * 答案代码如（A、B、C、D）
     */
    private String answerCode;

    /**
     * 所有的code/选项封装map
     */
    private Map<String ,String> contentTitle;

}

