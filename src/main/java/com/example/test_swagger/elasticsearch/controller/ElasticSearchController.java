package com.example.test_swagger.elasticsearch.controller;

import com.example.test_swagger.entity.PaAnswerRecordDTO;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shaoqk
 * @create 2021-06-02 11:39
 */
@Api(value = "ElasticSearchController", tags = {"ElasticSearch测试地址"})
@RestController
public class ElasticSearchController {

    public static void main(String[] args) {

        List<PaAnswerRecordDTO> list = new ArrayList<>();
//        list.add(new PaAnswerRecordDTO("003", 3));
//        list.add(new PaAnswerRecordDTO("001", 1));
//        list.add(new PaAnswerRecordDTO("002", null));

//        JSONArray arr = JSONUtil.createArray();
//        for (PaAnswerRecordDTO sysUser : list) {
//            if (sysUser != null && sysUser.getRaking() != null) {
//                arr.add(sysUser.getAddress());
//            }
//        }
//        System.out.println(arr);

        String a = "/2021-06-10由呼市开发1从小西街社区第2网格流动到车站东街社区第2网格;/2021-06-10由呼市开发1从小西街社区第2网格流动到羊群沟乡泥合子村网格";
        List<String> strings = Arrays.asList(a.split(";"));
        System.out.println(strings);
        
        int c = 2;
        String substring = a.substring(a.length() - 1);
        System.out.println(substring);
    }

}
