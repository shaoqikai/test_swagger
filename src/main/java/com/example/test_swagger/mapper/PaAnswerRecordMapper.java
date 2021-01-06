package com.example.test_swagger.mapper;

import com.example.test_swagger.entity.PaAnswerRecord;
import com.example.test_swagger.entity.PaAnswerRecordDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shaoqk
 * @create 2020-12-19 11:31
 */
@Mapper
public interface PaAnswerRecordMapper {

    void save(PaAnswerRecord paAnswerRecord);

    List<PaAnswerRecordDTO> getAnswerRaking(Integer screenYear);

    List<PaAnswerRecordDTO> getAnswerStatistics();

    List<PaAnswerRecordDTO> answerDaysFrom();

    /**
     * 本期数
     * @return
     */
    List<PaAnswerRecordDTO> answerToDay();

    /**
     * 上期数
     * @return
     */
    List<PaAnswerRecordDTO> answerYesterday();

}
