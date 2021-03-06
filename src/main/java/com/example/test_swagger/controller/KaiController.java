package com.example.test_swagger.controller;

import com.example.test_swagger.commont.ReturnInfo;
import com.example.test_swagger.entity.TbBrand;
import com.example.test_swagger.service.KaiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 邵祺锴
 * @create 2020-01-15 14:03
 */
@Api(value = "KaiController", tags = {"测试接口模板"})
@RestController
public class KaiController {

    private static Logger logger = LoggerFactory.getLogger(KaiController.class);

    @Autowired
    private KaiService service;

    @ApiOperation(value = "测试", notes = "测试接口")
    @RequestMapping(value = "/api/test/helloword", method = RequestMethod.DELETE)
    public String getData() {
        System.out.println("测试接口");
        return "测试接口是否可用";
    }

    @ApiOperation(value = "测试数据库", notes = "测试接口")
    @RequestMapping(value = "/api/test/SqlData", method = RequestMethod.GET)
    public String getSqlData(
            @ApiParam(name = "variantDesc", value = "变式名", required = true) @RequestParam(name = "variantDesc", required = true) String variantDesc,
            @ApiParam(name = "like", value = "变式名", required = false) @RequestParam(name = "like", required = false) String like) {
        return "你输入的id是" + variantDesc + ",输入的爱好是：" + like;
    }

    @ApiOperation(value = "查询所有", notes = "查询所有")
    @RequestMapping(value = "/api/test/findAll", method = RequestMethod.GET)
    public ReturnInfo findAll() {
        List<TbBrand> result = service.findAll();
        return ReturnInfo.oJ8K().put("result", result);
    }

    @ApiOperation(value = "根据ID查询", notes = "根据ID查询")
    @RequestMapping(value = "/api/test/findById", method = RequestMethod.PUT)
    public ReturnInfo findById(
            @ApiParam(name = "id", value = "查询数据的id", required = true) @RequestParam(name = "id", required = true) int id) {
        TbBrand byId = service.findById(id);
        return ReturnInfo.oJ8K().put("result", byId);
    }


    @ApiOperation(value = "根据ID查询名称", notes = "根据ID查询名称")
    @RequestMapping(value = "/api/test/findByName", method = RequestMethod.OPTIONS)
    public ReturnInfo findByName(
            @ApiParam(name = "id", value = "查询数据的id", required = true) @RequestParam(name = "id", required = true) int id) {
        String byName = service.findByName(id);
        return ReturnInfo.oJ8K().put("result", byName);
    }


}
