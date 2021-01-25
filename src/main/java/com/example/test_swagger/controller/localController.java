package com.example.test_swagger.controller;

import com.example.test_swagger.commont.Constant;
import com.example.test_swagger.service.KaiService;
import com.example.test_swagger.utils.ExportUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shaoqk
 * @create 2020-01-15 14:47
 */
@Api(value = "localController", tags = {"下载接口模板"})
@RestController
public class localController {

    @Autowired
    private KaiService service;

    private static Logger logger = LoggerFactory.getLogger(KaiController.class);

    @ApiOperation(value = "测试数据库", notes = "测试接口")
    @RequestMapping(value = "/api/test/local/ww", method = RequestMethod.GET)
    public void exportData(
            HttpServletResponse response,
            @ApiParam(name = "id", value = "数据的id", required = true) @RequestParam(name = "id", required = true) int id)
    {
        //设置请求
        response.setContentType("application/application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=\"" +
                Constant.MY_DEMO + id);
    }

    @ApiOperation(value = "按压缩包的方式下载zip", notes = "按压缩包的方式下载zip")
    @RequestMapping(value = "/api/test/ww/{id}", method = RequestMethod.GET)
    public void exportAnalysisSummaryByWw(HttpServletRequest request, HttpServletResponse response,
                                          @ApiParam(name = "id", value = "Task的ID", required = true) @PathVariable("id") int id)
    {
        try {
            // 生成的excel表格路径
            List<String> fileNameList = new ArrayList<>();
            response.setContentType("application/application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=\"");

            response.setHeader("Access-Control-Allow-Origin", "*");//* or origin as u prefer
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "PUT, POST, GET, OPTIONS, DELETE");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization");
            response.setHeader("XDomainRequestAllowed", "1");

            response.setHeader("Access-Control-Expose-Headers", "download-status,download-filename,download-message");

            // 将数据导入到excel表格中，并将生成的表格路径添加到fileNameList中
            ExportUtils.toExcelOne(request,
                    ExportUtils.getExcelTitle(Constant.SEGMENT_TYPE_PRC,
                            service.findByName(id)), fileNameList,service);

            ExportUtils.toExcelTwo(request,
                    ExportUtils.getExcelTitle(Constant.VARIANCE_ANGLYSIS_DETAIL,
                            service.findByName(id)), fileNameList,service);


            // 将生成的多个excel表格路径压缩成zip格式文件，并返回生成的临时zip文件路径
            String zipFilePath = ExportUtils.toZipFiles(request, fileNameList, "批量导出excel文件.zip");
            // zip文件创建成功
            if (!StringUtils.isEmpty(zipFilePath)) {
                try {
                    // 设置response头
                    ExportUtils.setResponseHeader(response, "数据文件.zip");
                    // 下载
                    ExportUtils.downloadZip(response.getOutputStream(), zipFilePath);
                } catch (final IOException e) {
                    logger.info("get outputStream from response failed");
                }
            }
        } catch (Exception e) {
            logger.info("API请求 /api/dm/ca/summary/ww/ 出现异常：" + e.getMessage());
        }
    }


}
