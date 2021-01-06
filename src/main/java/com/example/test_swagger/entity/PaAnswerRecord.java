package com.example.test_swagger.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author shaoqk
 * @create 2020-12-19 11:21
 */
@Data
@NoArgsConstructor
public class PaAnswerRecord {

    /**
     * ID
     */
    private Integer id;

    /**
     * 答题编号
     */
    private String recordNo;

    /**
     * 答题人id
     */
    private String userId;

    /**
     * 答题人姓名
     */
    private String userName;

    /**
     * 所在街道
     */
    private String address;

    /**
     * 题目总数
     */
    private Integer subjectTotal;

    /**
     * 答对数量
     */
    private Integer rightNum;

    /**
     * 获得积分
     */
    private Integer points;

    /**
     * 答题状态 0:未答题 1:已答题 2:未答完全
     */
    private String recordStatus;

    /**
     * 答题日期
     */
    private Date recordDate;

    /**
     * 答题时间
     */
    private Date recordTime;

    /**
     * 答题时间
     */
    private Integer creator;
}
