package com.example.test_swagger.mapper;

import com.example.test_swagger.entity.PaSubject;
import com.example.test_swagger.entity.PaSubjectQueryDTOTwo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author shaoqk
 * @create 2020-12-16 11:00
 */
@Mapper
public interface PaSubjectMapper {

    int saveAndSelectId(PaSubject paSubject);

    int findBySubjectContent(String subjectContent);


    List<PaSubjectQueryDTOTwo> subjectAll();

}
