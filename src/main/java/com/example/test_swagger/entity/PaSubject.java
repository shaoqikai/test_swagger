package com.example.test_swagger.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author shaoqk
 * @create 2020-12-16 13:11
 */
@Data
@NoArgsConstructor
public class PaSubject{
    private static final long serialVersionUID = 1L;



    private Integer id;
    private String subjectContent;
    private String answerCode;
    private String answerContent;

}
