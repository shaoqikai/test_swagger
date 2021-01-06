package com.example.test_swagger.entity;

import lombok.Data;

/**
 * @author shaoqk
 * @create 2020-12-14 14:20
 */
@Data
public class PaMessge {

    /**
     * 消息类型:01-爆料消息
     */
    private String messageType;

    /**
     * 消息主题（01:上报新的爆料,02:爆料受理,03:爆料处置,04:爆料评论,05:评论回复,06:预约通知）
     */
    private String messageTitle;

    /**
     * 消息来源:字典项(MESSAGE_SOURCE)
     */
    private String messageSource;

    /**
     * 消息内容
     */
    private String messageContent;

    /**
     * 关联的内容json字符串
     */
    private String refJson;

    /**
     * 用户ID
     */
    private Integer userId;
}
