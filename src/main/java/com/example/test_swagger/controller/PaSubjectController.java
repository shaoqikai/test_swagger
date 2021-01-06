package com.example.test_swagger.controller;

import com.example.test_swagger.commont.ReturnInfo;
import com.example.test_swagger.entity.PaSubjectAddDTO;
import com.example.test_swagger.service.PaSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shaoqk
 * @create 2020-12-16 10:51
 */

@Api(value = "PaSubjectController", tags = {"试题模板"})
@RestController
@Slf4j
public class PaSubjectController {

    @Autowired
    private PaSubjectService paSubjectService;

    @ApiOperation(value = "新增题目表答案表", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value = "/v1/save")
    public ReturnInfo addPaSubjectAndAnswer(@RequestBody PaSubjectAddDTO addDTO) {
        System.out.println("访问Controller-------------");
        paSubjectService.addPaSubjectAndAnswer(addDTO);
        return ReturnInfo.oJ8K().put("result", true);
    }

    @ApiOperation(value = "批量导入题目试题", notes = "批量导入题目试题")
    @RequestMapping(value = "/api/subject/import", method = RequestMethod.POST)
    public ReturnInfo importSubject(MultipartFile file) {
        String succeed = paSubjectService.importSubject(file);
        return ReturnInfo.oJ8K(succeed);
    }


    @ApiOperation("查询/随机返回试题")
    @GetMapping("/v1/{user_id}")
    public ReturnInfo findsubjectTotal(@PathVariable @ApiParam(value = "user_id", name = "答题人id", required = true) String user_id) throws Exception {
        Object o = paSubjectService.findsubjectTotal(user_id);
        System.out.println(user_id);
        return ReturnInfo.oJ8K().put("result", o);
    }

    @ApiOperation("从redis中获取所有试题")
    @GetMapping("/v1/xxs")
    public ReturnInfo xxs() throws Exception {
        Object list = paSubjectService.xxs();

        return ReturnInfo.oJ8K().put("result", list);
    }

    @ApiOperation(value = "下载试题导入模板", notes = "下载试题导入模板")
    @RequestMapping(value = "/api/subject/export", method = RequestMethod.POST)
    public ReturnInfo exportSubject(HttpServletResponse response) throws IOException {
        paSubjectService.export(response);
        return ReturnInfo.oJ8K().put("result", true);
    }
}
