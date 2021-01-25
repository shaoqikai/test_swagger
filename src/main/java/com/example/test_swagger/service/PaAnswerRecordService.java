package com.example.test_swagger.service;

import com.example.test_swagger.entity.PaAnswerQueryRightDTO;
import com.example.test_swagger.entity.PaAnswerRecord;
import com.example.test_swagger.entity.PaAnswerRecordAddDTO;
import com.example.test_swagger.entity.PaAnswerRecordDTO;
import com.example.test_swagger.mapper.PaAnswerMapper;
import com.example.test_swagger.mapper.PaAnswerRecordMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author shaoqk
 * @create 2020-12-19 11:17
 */
@Service
@Slf4j
public class PaAnswerRecordService {

    private static final String smsRedisPreFix = "PaAnswerRecordKey";


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PaAnswerRecordMapper paAnswerRecordMapper;

    @Autowired
    private PaAnswerMapper paAnswerMapper;

    public void add(PaAnswerRecordAddDTO addDTO) throws JsonProcessingException {
        String subjectId = null;
        if (addDTO.getAnswers() != null && addDTO.getAnswers().size() > 0) {
            subjectId = addDTO.getAnswers().get(0).getSubjectId().toString();
        }
        //存储规则 PaAnswerRecordKey + 用户ID + ~ +  试题ID 作为redis的key
        String key = smsRedisPreFix + addDTO.getUserId() + "~" + subjectId;
        log.info("存储试题key为:{}", key);
        redisTemplate.opsForValue().set(key, addDTO, 24, TimeUnit.HOURS);

        if (addDTO.getAnswerStatus().equals("1")) {
//            List<PaAnswerRecordAddDTO> paAnswerRecord = getPaAnswerRecord(addDTO.getUserId());
            int count = 0;
            PaAnswerRecord record = new PaAnswerRecord();
//            for (int i = 0; i < paAnswerRecord.size(); i++) {
//                System.out.println(paAnswerRecord.get(i).toString());
//                if (1 == paAnswerRecord.get(i).getAnswerYes()) {
//                    count += paAnswerRecord.get(i).getAnswerYes();
//                    record.setAddress(paAnswerRecord.get(i).getUserName());
//                }
//            }
            record.setId(count);
            paAnswerRecordMapper.save(record);
        }
    }

    public PaAnswerRecordAddDTO getPaAnswerRecord(String userId, Integer subjectId) throws JsonProcessingException {
        //拼接redis key
        String key = smsRedisPreFix + userId + "~*";
        Set<String> keys = redisTemplate.keys(key);
        // 批量获取数据
        List<Object> list = redisTemplate.opsForValue().multiGet(keys);
        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(list);
        List<PaAnswerRecordAddDTO> lendReco = mapper.readValue(s, new TypeReference<List<PaAnswerRecordAddDTO>>() {
        });
        PaAnswerRecordAddDTO newpaAnswer = new PaAnswerRecordAddDTO();
        for (int i = 0; i < lendReco.size(); i++) {
            if (subjectId.equals(lendReco.get(i).getAnswers().get(i).getSubjectId())) {
                BeanUtils.copyProperties(lendReco.get(i), newpaAnswer);
            }
        }

        return newpaAnswer;


    }

    public List<PaAnswerRecordAddDTO> getPaAnswerRecordRedis(String userId) throws JsonProcessingException {
        //拼接redis key
        String key = smsRedisPreFix + "*";
        Set<String> keys = redisTemplate.keys(key);
        ObjectMapper mapper = new ObjectMapper();
        List<Object> list = redisTemplate.opsForValue().multiGet(keys);
        String s = mapper.writeValueAsString(list);
        // 字符串转换为对象
        List<PaAnswerRecordAddDTO> lendReco =
                mapper.readValue(s, new TypeReference<List<PaAnswerRecordAddDTO>>() {
                });

        // 根据ID进行分组
        Map<String, List<PaAnswerRecordAddDTO>> groupBy =
                lendReco.stream().collect(Collectors.groupingBy(PaAnswerRecordAddDTO::getUserId));

        for (Map.Entry<String, List<PaAnswerRecordAddDTO>> maps : groupBy.entrySet()) {
            int count = 0;
            int a = 6;
//            ArrayList rtnList = (ArrayList) maps.getValue();
            List<PaAnswerRecordAddDTO> rtnList = maps.getValue();
            PaAnswerRecord paAnswerRecord = new PaAnswerRecord();
            for (PaAnswerRecordAddDTO paAnswerRecordAddDTO : rtnList) {
                count += paAnswerRecordAddDTO.getAnswerYes();
                paAnswerRecord.setId(a++);
                paAnswerRecord.setPoints(count);
                paAnswerRecord.setAddress(paAnswerRecordAddDTO.getUserName());
            }
            paAnswerRecordMapper.save(paAnswerRecord);
        }

        return lendReco;
    }

    public PaAnswerQueryRightDTO getSubjectRight(String userId) throws JsonProcessingException {
        //拼接redis key
        String key = smsRedisPreFix + userId + "~*";
        Set<String> keys = redisTemplate.keys(key);

        // 批量获取数据
        List<Object> list = redisTemplate.opsForValue().multiGet(keys);

        ObjectMapper mapper = new ObjectMapper();
        String s = mapper.writeValueAsString(list);
        List<PaAnswerRecordAddDTO> lendReco = mapper.readValue(s, new TypeReference<List<PaAnswerRecordAddDTO>>() {
        });
        PaAnswerQueryRightDTO newPaAnswer = new PaAnswerQueryRightDTO();
        int answerYes = 0;
        int answerNO = 0;
        // 循环判断试题正确数错误数
        for (int i = 0; i < lendReco.size(); i++) {
            if (lendReco.get(i).getAnswerYes().equals(0)) {
                answerNO++;
            } else {
                answerYes++;
            }
        }
        newPaAnswer.setAnswerYes(answerYes);
        newPaAnswer.setAnswerNO(answerNO);
        log.info("根据用户ID从redis获取答对答错数:{}", newPaAnswer);
        return newPaAnswer;
    }

    public List<PaAnswerRecordDTO> getAnswerRaking(Integer screenYear) {

        List<PaAnswerRecordDTO> paAnswerRecordDTOList =
                paAnswerRecordMapper.getAnswerRaking(screenYear);
        if (CollectionUtils.isEmpty(paAnswerRecordDTOList)) {
            throw new ArithmeticException("平安竞答查询排名异常，请联系管理员");
        }
        return paAnswerRecordDTOList;
    }

    public List<PaAnswerRecordDTO> getAnswerStatistics() {
        List<PaAnswerRecordDTO> list =
                paAnswerRecordMapper.getAnswerStatistics();
        if (CollectionUtils.isEmpty(list)) {
            throw new ArithmeticException("平安竞答地区排名异常，请联系管理员");
        }
        return list;
    }

    public Map<String,Object> answerDaysFrom() {
        // 存放日期的map key为day时间
        Map<String,Object> dayMap = new HashMap<>();
        List<PaAnswerRecordDTO> daylist = paAnswerRecordMapper.answerDaysFrom();
        List<PaAnswerRecordDTO> toWeek = getToWeek();

        /*List<Map<String, Object>> userMapInfo = userDeptMap.stream().filter(item -> {
            return item.get("userId").toString().equals(this.getUserId().toString());
        }).collect(Collectors.toList());*/

        // 生成近7天 key为日期的map
        for (PaAnswerRecordDTO item : toWeek) {
            dayMap.put(item.getDeteLine(),item);
        }
        for(PaAnswerRecordDTO item : daylist){
            if(dayMap.get(item.getDeteLine())!=null){
                dayMap.put(item.getDeteLine(),item);
            }
        }
        return dayMap;
    }

    /**
     * 根据当前时间获取前一周开始结束时间
     *
     * @return
     */
    public static Map getDaySevenRange() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map condition = new HashedMap();
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        condition.put("endDate", df.format(calendar.getTime()));
        calendar.set(Calendar.HOUR_OF_DAY, -168);
        condition.put("startDate", df.format(calendar.getTime()));
        return condition;
    }

    /**
     * 根据当前时间获取前一周时间段
     *
     * @return
     */
    public static List<PaAnswerRecordDTO> getToWeek() {
        List<PaAnswerRecordDTO> list = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i <= 7; i++) {
            PaAnswerRecordDTO paAnswerRecordDTO = new PaAnswerRecordDTO();
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, -i);
            Date d = c.getTime();
            String day = format.format(d);
            paAnswerRecordDTO.setDeteLine(day);
            paAnswerRecordDTO.setRingRatio("0.00");
            list.add(paAnswerRecordDTO);
        }
        return list;
    }



}