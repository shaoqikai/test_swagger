package com.example.test_swagger.controller;

import com.example.test_swagger.commont.ReturnInfo;
import com.example.test_swagger.entity.CaseCount;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author shaoqk
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

    public static void main(String[] args) {
        int a = 6;
        int b = 5;
        CaseCount count = new CaseCount();
        count.setType("a");
        count.setCount(a);
        CaseCount countc = new CaseCount();
        countc.setType("b");
        countc.setCount(b);
        List<CaseCount> list = new ArrayList<>();
        list.add(count);
        list.add(countc);
        System.err.println(biJiao(list));

    }

    /**
     * @param list 查询的用户下面关联的数量
     * @return 返回下一个分派的用户编码
     */
    private static String biJiao(List<CaseCount> list) {
        // 创建返回对象
        String result = "";
        if (list != null && !list.isEmpty()) {
            // 当只查询到一条数据时不判断直接派遣
            if (list.size() == 1) {
                return list.get(0).getType();
            }
            // 多条数据时判断下一个分给谁
            int count = list.get(0).getCount();
            for (CaseCount caseCount : list) {
                if (caseCount.getCount() <= count) {
                    count = caseCount.getCount();
                    result = caseCount.getType();
                }
            }
        }
        return result;
    }


}
