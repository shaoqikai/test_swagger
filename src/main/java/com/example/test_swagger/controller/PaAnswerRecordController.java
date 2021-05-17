package com.example.test_swagger.controller;

import com.example.test_swagger.commont.ReturnInfo;
import com.example.test_swagger.entity.*;
import com.example.test_swagger.service.PaAnswerRecordService;
import com.example.test_swagger.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static io.lettuce.core.LettuceStrings.string;


/**
 * @author shaoqk
 * @create 2020-12-19 11:16
 */
@Api(value = "paAnswerRecordController", tags = {"答案记录表管理"})
@RestController("/answer")
@Slf4j
public class PaAnswerRecordController {

    @Autowired
    private PaAnswerRecordService paAnswerRecordService;

    @ApiOperation(value = "新增答题记录表", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/v1/saveAnswer")
    public ReturnInfo addPaAnswerRecord(@RequestBody PaAnswerRecordAddDTO addDTO) throws JsonProcessingException {
        paAnswerRecordService.add(addDTO);
        return ReturnInfo.oJ8K().put("result", true);
    }

    @ApiOperation(value = "根据用户ID获取答题记录表", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/v1/getPaAnswerRecord")
    public ReturnInfo getPaAnswerRecord(@RequestBody PaAnswerRecordAddDTO addDTO) throws JsonProcessingException {

        if (addDTO.getUserId() == null || "".equals(addDTO.getUserId())) {
            return ReturnInfo.error(500, "用户ID不能为空");
        }

        PaAnswerRecordAddDTO paAnswerRecord =
                paAnswerRecordService.getPaAnswerRecord(addDTO.getUserId(), addDTO.getAnswers().get(0).getSubjectId());
        return ReturnInfo.oJ8K().put("result", paAnswerRecord);
    }


    @ApiOperation(value = "测试", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/v1/ceshi")
    public List<PaAnswerRecordAddDTO> ceshi(@RequestBody PaAnswerRecordAddDTO addDTO) throws JsonProcessingException {
        List<PaAnswerRecordAddDTO> paAnswerRecord =
                paAnswerRecordService.getPaAnswerRecordRedis(addDTO.getUserId());
        return paAnswerRecord;
    }

    /**
     * 根据用户ID获取题目正确数
     *
     * @param
     * @return
     */
    @ApiOperation(value = "根据用户ID获取题目正确数", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/v1/getredis/{user_id}")
    public PaAnswerQueryRightDTO getredis(@RequestBody PaAnswerRecordAddDTO addDTO) throws JsonProcessingException {
        PaAnswerQueryRightDTO paAnswerRecord =
                paAnswerRecordService.getSubjectRight(addDTO.getUserId());
        return paAnswerRecord;
    }

    /**
     * 平安竞答排名
     */
    @ApiOperation(value = "平安竞答排名", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/v1/getAnswerStatistics")
    public List<PaAnswerRecordDTO> getAnswerStatistics() {

        List<PaAnswerRecordDTO> paAnswerRecord =
                paAnswerRecordService.getAnswerStatistics();
        return paAnswerRecord;
    }

    /**
     * 平安竞答统计分析
     */
    @ApiOperation(value = "平安竞答统计分析", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/v1/getAnswerRaking")
    public List<PaAnswerRecordDTO> getAnswerRaking(@RequestBody PaAnswerYear paAnswerYear) {
        if (paAnswerYear.getScreenYear() == null || "".equals(paAnswerYear.getScreenYear())) {
            throw new ArithmeticException("请输入查询月份还是年份");
        }
        List<PaAnswerRecordDTO> paAnswerRecord =
                paAnswerRecordService.getAnswerRaking(paAnswerYear.getScreenYear());
        return paAnswerRecord;
    }


    /**
     * 竞答日环比
     *
     * @return
     */
    @ApiOperation("竞答日环比")
    @GetMapping("/analyze/answerDaysFrom")
    public Map<String, Object> answerDaysFrom() {
        return paAnswerRecordService.answerDaysFrom();
    }

    public static void main(String[] args) throws ParseException {
//        PaAnswer paAnswer = new PaAnswer(1,"12","B",23,1,12,"上海");
//        Integer poris = 10 * 3 + (10 - 10);
//        System.out.println(poris);
//
//
//        Integer a = 1;
//        String b = "1";
//        boolean equals = a.toString().equals(b);
//        System.out.println(equals);
//
//
//        String s1 = "2021-03-04 11:21:16";
//        String s2 = "2021-03-05 11:21:16";
//        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
//        Date dd = fmt.parse(s1);
//        Date ss = fmt.parse(s2);
//
//        System.out.println(isEffectiveDate(new Date(), dd, ss));
//        String betweenTime = getBetweenTime(1, "0");
//        System.out.println("type为0---------" + betweenTime);
//
//        String between = getBetweenTime(1, "1");
//        System.out.println("type为1---------" + between);
//        PortrayQuery query = new PortrayQuery();
//        System.out.println(decideStatisticType(query).toString()+"----------------------------------------");
//        List<PortrayQuery> result = new ArrayList<>();
//        result.add(new PortrayQuery(2, "2020-12-15"));
//        result.add(new PortrayQuery(11, "2020-12-16"));
//        result.add(new PortrayQuery(1, "2020-12-17"));
//        result.add(new PortrayQuery(29, "2020-12-18"));
//        result.add(new PortrayQuery(1, "2021-01-19"));
//        result.add(new PortrayQuery(2, "2021-01-20"));
//        result.add(new PortrayQuery(33, "2021-03-21"));
//        Map<String, Object> map = new HashMap<>();

//
//        List<TbBrand> list = new ArrayList<>();
//        list.add(new TbBrand(1, "2021-01-03"));
//        list.add(new TbBrand(5, "2021-01-03"));
//        list.add(new TbBrand(2, "2021-01-03"));
//        list.add(new TbBrand(1, "2021-01-04"));
//
//        List<TbBrand> list2 = new ArrayList<>();
//        list2.add(new TbBrand(0, "2021-01-03"));
//        list2.add(new TbBrand(5, "2021-01-03"));
//        list2.add(new TbBrand(2, "2021-01-03"));
//        list.add(new TbBrand(1, "2021-01-04"));
//        list.add(new TbBrand(2, "2021-01-07"));
//        list.add(new TbBrand(1, "2021-01-07"));
//        list.add(new TbBrand(1, "2021-01-19"));
//        list.add(new TbBrand(1, "2021-01-19"));
//        list.add(new TbBrand(1, "2021-03-07"));
//        list.add(new TbBrand(5, "2021-03-07"));
//        list.add(new TbBrand(3, "2021-03-07"));
//        list.add(new TbBrand(4, "2021-03-07"));
//        int count =0;
//        for (TbBrand tbBrand : list2) {
//             count += tbBrand.getNum() ;
//        }
//        System.out.println(count);
//        Integer score0 = 0;
//
//        for (PortrayQuery query : result) {
//            if (query.getType().equals("2020-12-15")){
//                score0 = 1;
//                map.put("score0", score0);
//            }else if (query.getType().equals("2020-12-16")){
//                score0 = 2;
//                map.put("score1", score0);
//            }
//        }
//
//        System.out.println(map.toString());
//        Map daySevenRange =  DateUtils.getDaySevenRange();
//        System.out.println(daySevenRange.get("startDate"));

        long s1 = 24;
        System.out.println(string(s1));
        System.out.println(s1+"");

        PortrayQuery query = new PortrayQuery();
        PortrayQuery query1 = DateUtils.decideStatisticType(query);
        System.out.println(query1);

        if (!"1".equals(null)){
            System.out.println("true");
        }
    }


}
