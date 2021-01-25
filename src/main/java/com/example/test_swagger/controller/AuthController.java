package com.example.test_swagger.controller;

import com.example.test_swagger.entity.SysUser;
import com.example.test_swagger.service.KaiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author shaoqk
 * @create 2021-01-20 11:26
 * 微信鉴权
 */
@Api(value = "paAnswerRecordController", tags = {"答案记录表管理"})
@RestController("/weChatAuthen")
@Slf4j
public class AuthController {

    @Autowired
    private KaiService service;

    /**
     * 微信登录接口
     *
     * @param code 前端获取到的code
     * @return 返回用户信息
     */
    @ApiOperation(value = "小程序登录接口", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PostMapping("/wxLogin")
    public Map<String, Object> login(@RequestParam("code") String code) {
        Map<String, Object> resultMap = service.login(code);
        return resultMap;
    }

    /**
     * 保存用户信息
     */
    @RequestMapping("insertWx")
    public SysUser insertUser(SysUser user) {
        return service.insertUser(user);
    }

}
