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

import java.math.BigInteger;
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
//        List<Map<String, Object>> dataList = page.getResult();
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        Map<String, Object> map1 = new HashMap<String, Object>();
//        map1.put("id", 1);
//        map1.put("value", 2);
//        list.add(map1);
//
//        Map<String, Object> map2 = new HashMap<String, Object>();
//        map2.put("id", 1);
//        map2.put("value", 5);
//        list.add(map2);
//
//        Map<String, Object> map3 = new HashMap<String, Object>();
//        map3.put("id", 2);
//        map3.put("value", 5);
//        list.add(map3);
//
//        Map<String, Object> map4 = new HashMap<String, Object>();
//        map4.put("id", 2);
//        map4.put("value", 4);
//        list.add(map4);
//
//        Map<String, Object> map5 = new HashMap<String, Object>();
//        map5.put("id", 1);
//        map5.put("value", 4);
//        list.add(map5);
//
//        //id相等value相加（合并id相同数据）
//        System.out.println("原始数据："+list);
//
//        //方法一
//        Map<String, Object> result1 = new HashMap<String, Object>();
//        for(Map<String, Object> map : list){
//            String id = map.get("id").toString();
//            Long value = Long.parseLong(map.get("value").toString());
//            if(result1.containsKey(id)){
//                Long temp = Long.parseLong(result1.get(id).toString());
//                value += temp;
//            }
//            result1.put(id, value);
//        }
//        System.out.println("合并后的数据："+result1);

        //方法二
//        Map<String, Map<String, Object>> result2 = new HashMap<String, Map<String,Object>>();
//        for(Map<String, Object> map : list){
//            String id = map.get("id").toString();
//            Long value = Long.parseLong(map.get("value").toString());
//            if(result2.containsKey(id)){
//                Long temp = Long.parseLong(result2.get(id).get("value").toString());
//                value += temp;
//                result2.get(id).put("value", value);
//                continue;
//            }
//            result2.put(id, map);
//        }
//        System.out.println("合并后的数据2："+result2);
//        String cc = "1,11,20,147,665";
//        String[] split = cc.split(",");
//        List<String> resultList= new ArrayList<>(Arrays.asList(cc.split(",")));
//        System.out.println(resultList.toString());

        Integer a = 999999999;
        BigInteger bigInteger = new BigInteger("99999999999999999999999999999999999999999999999");
        System.out.println(a);
        System.out.println(bigInteger);
    }

}
