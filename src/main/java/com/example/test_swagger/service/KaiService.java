package com.example.test_swagger.service;

import com.alibaba.fastjson.JSONObject;
import com.example.test_swagger.entity.SysUser;
import com.example.test_swagger.entity.TbBrand;
import com.example.test_swagger.mapper.KaiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.util.*;

/**
 * @author shaoqk
 * @create 2020-01-15 14:03
 */
@Service
public class KaiService {

    @Autowired
    private KaiMapper mapper;

//    @Autowired
//    private RestTemplate restTemplate;

    @Value("${wechat.loginTokenUrl}")
    private String loginUrl;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secret}")
    private String secret;

    /**
     * 微信登录接口
     *
     * @param code
     * @return
     */
    public Map<String, Object> login(String code) {
        // 创建返回Map
        Map<String, Object> resultMap = new HashMap<>();
        System.out.println("前端传递code值：" + code);

        String loginTokenUrl = loginUrl + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        try {
//            JSONObject result = (JSONObject) this.restTemplate.postForObject(loginTokenUrl, null, JSONObject.class);
            // Delete
            JSONObject result = new JSONObject();
            System.out.println("微信返回的结果" + result);
            // 进行封装
            resultMap.put("data", result);

            // 对返回结果进行解析
            JSONObject json_test = JSONObject.parseObject(String.valueOf(result));
            String openid = json_test.getString("openid");
            String sessionKey = json_test.getString("session_key");
            System.out.println("oppID：" + openid);
            System.out.println("sessionKey：" + sessionKey);
            // 根据openid去db查询用户信息
            SysUser user = mapper.selectById(openid);
            System.out.println("用户的信息" + user);

            if (Objects.isNull(user)) {// 如果user为null，说明用户第一次登录 ,调用注册接口进行注册
                resultMap.put("state", 2000);
                resultMap.put("data", openid);
                resultMap.put("sessionKey", sessionKey);
                resultMap.put("message", "未查询到用户信息~");
            } else {
                resultMap.put("state", 2000);
                resultMap.put("data", openid);
                resultMap.put("sessionKey", sessionKey);
                resultMap.put("user", user);// 查询到的用户信息封装返回
                resultMap.put("message", "该用户已存在~");
                return resultMap;
            }

            if (StringUtils.isEmpty(openid)) {
                resultMap.put("state", 2000);
                resultMap.put("message", "未获取到openid");
                return resultMap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 保存微信用户信息接口
     *
     * @param user
     * @return
     */
    public SysUser insertUser(SysUser user) {
        SysUser userOld = mapper.selectById(user.getOpenId());
        if (userOld == null) {
            mapper.insertUser(user);
        } else {
            mapper.updateUser(user);
        }
        return null;
    }

    /**
     * 解析电话号码
     *
     * @param session_key
     * @param encryptedData
     * @param iv
     * @return
     */
    public JSONObject getPhoneNumber(String session_key, String encryptedData, String iv) {
        byte[] dataByte = Base64.getMimeDecoder().decode(encryptedData);
        byte[] keyByte = Base64.getMimeDecoder().decode(session_key);
        byte[] ivByte = Base64.getMimeDecoder().decode(iv);
        try {

            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
//            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<TbBrand> findAll() {
        return mapper.findAll();
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public TbBrand findById(int id) {
        return mapper.findById(id);
    }

    public String findByName(int id) {
        return mapper.findByName(id);
    }

    /**
     * 合并升序列表
     */
    public void shengXU() {
        int[] nums1 = {1, 2, 3, 4};
        int[] nums2 = {1, 4, 6};
        int[] total = new int[nums1.length + nums2.length];

        int indexForOne = 0, indexForTwo = 0;

        // 两个数组对比
        for (int i = 0; i < total.length; i++) {
            if (indexForOne == nums1.length) {
                // 如果第一个数组中的数取完直接从第二个取
                total[i] = nums2[indexForTwo];
                indexForTwo++;
            } else if (indexForTwo == nums2.length) {
                total[i] = nums1[indexForOne];
                indexForOne++;
            } else if (nums1[indexForOne] <= nums2[indexForTwo]) {
                total[i] = nums1[indexForOne];
                indexForOne++;
            } else {
                total[i] = nums2[indexForTwo];
                indexForTwo++;
            }
            System.out.print(total[i] + "-");
        }


    }
}


