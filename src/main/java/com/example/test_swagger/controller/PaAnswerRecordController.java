package com.example.test_swagger.controller;

import com.example.test_swagger.commont.ReturnInfo;
import com.example.test_swagger.entity.PaAnswerQueryRightDTO;
import com.example.test_swagger.entity.PaAnswerRecordAddDTO;
import com.example.test_swagger.entity.PaAnswerRecordDTO;
import com.example.test_swagger.entity.PaAnswerYear;
import com.example.test_swagger.service.PaAnswerRecordService;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
        String betweenTime = getBetweenTime(1, "0");
        System.out.println("type为0---------" + betweenTime);

        String between = getBetweenTime(1, "1");
        System.out.println("type为1---------" + between);

    }


    /**
     * 获取近n月/年的时间跨度
     *
     * @return
     */
    public static String getBetweenTime(Integer n, String type) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        //过去一月
        c.setTime(new Date());
        //判断查询类型
        switch (type) {
            //月
            case "0":
                c.add(Calendar.MONTH, -n);
                break;
            //年
            case "1":
                c.add(Calendar.YEAR, -n);
                break;
            default:
        }
        Date m = c.getTime();
        String result = format.format(m);
        return result;
    }


    /**
     * 判断时间是否在某个区间内
     *
     * @param nowTime
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }
}
