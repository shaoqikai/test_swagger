package com.example.test_swagger.service;

import com.alibaba.fastjson.JSONObject;
import com.example.test_swagger.entity.PaAnswer;
import com.example.test_swagger.entity.PaSubject;
import com.example.test_swagger.entity.PaSubjectAddDTO;
import com.example.test_swagger.entity.PaSubjectQueryDTOTwo;
import com.example.test_swagger.mapper.PaAnswerMapper;
import com.example.test_swagger.mapper.PaSubjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author shaoqk
 * @create 2020-12-16 10:56
 */
@Service
@Slf4j
public class PaSubjectService {

    @Autowired
    private PaSubjectMapper paSubjectMapper;

    @Autowired
    private PaAnswerMapper paAnswerMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    private static final String smsRedisPreFix = "SubjectRandomKey";

    public void addPaSubjectAndAnswer(PaSubjectAddDTO addDTO) {
        System.out.println("访问PaSubjectService层");
        int length = addDTO.getAnswerContent().length;

        PaSubject paSubject = new PaSubject();

        paSubject.setId(90);
        paSubject.setSubjectContent(addDTO.getSubjectContent());
        paSubject.setAnswerCode(addDTO.getAnswerCode());
        paSubject.setAnswerContent(addDTO.getAnswerRight());

        // 插入到题目表并返回ID
        int status = paSubjectMapper.saveAndSelectId(paSubject);
        int subjectId = 0;
        if (status == 1) {
            subjectId = paSubjectMapper.findBySubjectContent(addDTO.getSubjectContent());
        }

        // 数据插入到答案表里
        PaAnswer paAnswer = new PaAnswer();
        int a = 90;
        for (int i = 0; i < addDTO.getAnswerContent().length; i++) {
            paAnswer.setId(a++);

            String[] split = addDTO.getAnswerContent()[i].split("\\.");
            paAnswer.setAnswerCode(split[0]);
            paAnswer.setAnswerContent(split[1]);
            if (addDTO.getAnswerCode().equals(split[0])) {
                paAnswer.setAnswerRight(1);
            } else {
                paAnswer.setAnswerRight(0);
            }
            paAnswer.setSubjectId(subjectId);
            paAnswerMapper.saveAnswer(paAnswer);
        }

    }

    public String importSubject(MultipartFile file) {
        XSSFWorkbook workbook = null;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());

            XSSFSheet sheetAt = workbook.getSheetAt(0);
            int lastRowNum = sheetAt.getLastRowNum();
            String strsub = file.getOriginalFilename().substring(file.getOriginalFilename().length() - 4);
            if (!".xls".equals(strsub) && !strsub.equals("xlsx")) {
                return "请上传正确的Excel文档！";
            }

            if (lastRowNum == 0) {
                return "Exclel文档没有数据！";
            }
            int subjectId = 0;
            int a = 80;
            int b = 80;

            PaSubject paSubject = new PaSubject();
            PaAnswer paAnswer = new PaAnswer();
            XSSFRow row = null;
            for (int i = 1; i <= lastRowNum; i++) {
                row = sheetAt.getRow(i);

                paSubject.setId(a++);

                if (row.getCell(7).getStringCellValue().equals(row.getCell(1).getStringCellValue())) {
                    paSubject.setAnswerCode("A");
                } else if (row.getCell(7).getStringCellValue().equals(row.getCell(2).getStringCellValue())) {
                    paSubject.setAnswerCode("B");
                } else if (row.getCell(7).getStringCellValue().equals(row.getCell(3).getStringCellValue())) {
                    paSubject.setAnswerCode("C");
                } else if (row.getCell(7).getStringCellValue().equals(row.getCell(4).getStringCellValue())) {
                    paSubject.setAnswerCode("D");
                } else if (row.getCell(7).getStringCellValue().equals(row.getCell(5).getStringCellValue())) {
                    paSubject.setAnswerCode("E");
                } else if (row.getCell(7).getStringCellValue().equals(row.getCell(6).getStringCellValue())) {
                    paSubject.setAnswerCode("F");
                }

                paSubject.setSubjectContent(row.getCell(0).getStringCellValue());
                paSubject.setAnswerContent(row.getCell(7).getStringCellValue());
                // 添加到题目表
                int i1 = paSubjectMapper.saveAndSelectId(paSubject);
                subjectId = paSubjectMapper.findBySubjectContent(row.getCell(0).getStringCellValue());

                b++;
                if (row.getCell(1) == null || row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK) {
                    return "答案选项A不能为空！";
                }
                saveCell(row, 1, paAnswer, subjectId, b);
                if (row.getCell(2) == null || row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK) {
                    return "答案选项B不能为空！";
                }
                b++;
                saveCell(row, 2, paAnswer, subjectId, b);


                b++;
                if (row.getCell(3) == null || row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK) {
                    continue;
                } else {
                    saveCell(row, 3, paAnswer, subjectId, b);
                }

                b++;
                if (row.getCell(4) == null || row.getCell(4).getCellType() == Cell.CELL_TYPE_BLANK) {
                    continue;
                } else {
                    saveCell(row, 4, paAnswer, subjectId, b);
                }

                b++;
                if (row.getCell(5) == null || row.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK) {
                    continue;
                } else {
                    saveCell(row, 5, paAnswer, subjectId, b);
                }

                b++;
                if (row.getCell(6) == null || row.getCell(6).getCellType() == Cell.CELL_TYPE_BLANK) {
                    continue;
                } else {
                    saveCell(row, 6, paAnswer, subjectId, b);
                }


            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传成功";
    }


    public void saveCell(XSSFRow row, int num, PaAnswer paAnswer, int subjectId, int b) {
        paAnswer.setId(b);
        paAnswer.setAnswerContent(row.getCell(num).getStringCellValue());
        paAnswer.setSubjectId(subjectId);
        switch (num) {
            case 1:
                paAnswer.setAnswerCode("A");
                break;
            case 2:
                paAnswer.setAnswerCode("B");
                break;
            case 3:
                paAnswer.setAnswerCode("C");
                break;
            case 4:
                paAnswer.setAnswerCode("D");
                break;
            case 5:
                paAnswer.setAnswerCode("E");
                break;
            case 6:
                paAnswer.setAnswerCode("F");
                break;
        }

        if (row.getCell(7).getStringCellValue().equals(row.getCell(num).getStringCellValue())) {
            paAnswer.setAnswerRight(1);
        } else {
            paAnswer.setAnswerRight(0);
        }
        paAnswerMapper.saveAnswer(paAnswer);

    }

    public Object findsubjectTotal(@RequestParam String user_id) {
        List<PaSubjectQueryDTOTwo> oldList = paSubjectMapper.subjectAll();
        // 随机获取10道题

        List<PaSubjectQueryDTOTwo> subjectlist = new ArrayList<>();

        for (PaSubjectQueryDTOTwo two : oldList) {
            PaSubjectQueryDTOTwo paSubjectQueryDTOTwo = new PaSubjectQueryDTOTwo();
            Map<String, String> contentTitleMap = new HashMap<>();
            paSubjectQueryDTOTwo.setSubjectContent(two.getSubjectContent());
            paSubjectQueryDTOTwo.setAnswerCode(two.getAnswerCode());
            paSubjectQueryDTOTwo.setAnswerRight(two.getAnswerRight());
            List<String> stringA = Arrays.asList(two.getCodes().split("/"));
            List<String> stringB = Arrays.asList(two.getAnswerContent().split("/"));
            for (int i = 0; i < stringA.size(); i++) {
                contentTitleMap.put(stringA.get(i), stringB.get(i));
                paSubjectQueryDTOTwo.setContentTitle(contentTitleMap);
                subjectlist.add(paSubjectQueryDTOTwo);
            }
        }
        String userId = user_id;
        List<Object> objectList = getRandom(subjectlist, 10);
        //存储规则 PaAnswerRecordKey +  ~ + 用户ID 作为redis的key
        String key = smsRedisPreFix + "~" + userId;
        redisTemplate.opsForValue().set(key, objectList, 24, TimeUnit.HOURS);
        return objectList;
    /*
            根据其中一个字段去重
            List<PaSubjectQueryDTOTwo> list1=
                    list.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PaSubjectQueryDTOTwo :: getSubjectContent))), ArrayList::new));
        */

    }

    /**
     * 抽取选择题试题
     *
     * @param questionList
     * @param n
     * @return
     */
    private List<Object> getRandom(List<PaSubjectQueryDTOTwo> questionList, int n) {
        List backList = null;
        backList = new ArrayList<>();
        Random random = new Random();
        int backSum = 0;
        if (questionList.size() >= n) {
            backSum = n;
        } else {
            backSum = questionList.size();
        }
        for (int i = 0; i < backSum; i++) {
//       随机数的范围为0-list.size()-1
            int target = random.nextInt(questionList.size());
            backList.add(questionList.get(target));
            questionList.remove(target);
        }
        return backList;
    }


    public Object xxs() {
        //存储规则 PaAnswerRecordKey +  ~ + 用户ID 作为redis的key
        String userId = "{user_id}";
        String key = "SubjectRandomKey" + "~" + userId;
        Object o = redisTemplate.opsForValue().get(key);
        return o;
    }



    public void exportSubject(HttpServletResponse request) {
        JSONObject result = new JSONObject();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet();
            XSSFRow row = sheet.createRow(0);
            String[] qrr = { "编号", "商家名称", "库存", "商家类型", "商品状态", "包邮城市", "商家名称", "时间", "商品详情" };
            for (int i = 0; i < qrr.length; i++) {
                row.createCell(i).setCellValue(qrr[i]);
            }

            workbook.write(new FileOutputStream("C://xs.xls"));
            workbook.close();
            result.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("errorMsg", "对不起，操作失败");
        }
//        WriterUtil.write(response, result.toString());
    }

    public void export( HttpServletResponse res) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        XSSFRow row = sheet.createRow(0);
        String[] qrr = { "题目内容（必填）", "答案A（必填）", "答案B（必填）", "答案C", "答案D", "答案E", "答案F", "正确答案（必填）" };
        CellStyle cellStyle2 = workbook.createCellStyle();
        sheet.setColumnWidth(0, 50*100);
        sheet.setColumnWidth(1, 50*100);
        sheet.setColumnWidth(2, 50*100);
        sheet.setColumnWidth(3, 50*100);
        sheet.setColumnWidth(4, 50*100);
        sheet.setColumnWidth(5, 50*100);
        sheet.setColumnWidth(6, 50*100);
        sheet.setColumnWidth(7, 50*100);
        cellStyle2.setWrapText(true);

        for (int i = 0; i < qrr.length; i++) {
            row.createCell(i).setCellValue(qrr[i]);

        }

        //将excel的数据写入文件
        ByteArrayOutputStream fos = null;
        byte[] retArr = null;
        try {
            fos = new ByteArrayOutputStream();
            workbook.write(fos);
            retArr = fos.toByteArray();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        OutputStream os = res.getOutputStream();
        try {
            res.reset();
            res.setHeader("Content-Disposition", "attachment; filename=试题导入模板.xlsx");//要保存的文件名
            res.setContentType("application/octet-stream; charset=utf-8");
            os.write(retArr);
            os.flush();
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }

}
