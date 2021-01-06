package com.example.test_swagger.mapper;

import com.example.test_swagger.entity.PaAnswer;
import org.apache.ibatis.annotations.Mapper;

/**
 * 答案表(PaAnswer)表数据库访问层
 *
 * @author makejava
 * @since 2020-12-08 17:11:47
 */
@Mapper
public interface PaAnswerMapper {

    void saveAnswer(PaAnswer paAnswer);

}