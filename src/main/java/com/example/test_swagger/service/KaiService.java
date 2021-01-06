package com.example.test_swagger.service;

import com.example.test_swagger.entity.TbBrand;
import com.example.test_swagger.mapper.KaiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author shaoqk
 * @create 2020-01-15 14:03
 */
@Service
public class KaiService {

    @Autowired
    private KaiMapper mapper;

    //查询所有
    public List<TbBrand> findAll() {
        return mapper.findAll();
    }

    //根据ID查询
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


    /**
     * string时间转为Date
     *
     * @param time
     * @param type 0-年月日时分秒 1-年月日
     * @return
     */
    public static Date StringChangeDate(String time, String type) {
        SimpleDateFormat sdf;
        if (type.equals("0")) {
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date date = null;
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 判断时间是否超过24小时
     *
     * @param date1
     * @param date2
     * @return
     * @throws Exception
     */
    public static boolean judgmentDate(Date date1, Date date2) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date start = date1;
        Date end = date2;
        long cha = end.getTime() - start.getTime();
        if (cha < 0) {
            return false;
        }

        double result = cha * 1.0 / (1000 * 60 * 60);

        if (result <= 24) {
            return true;
        } else {
            return false;
        }
    }


    public static void main(String[] args) throws Exception {

        Date date1 = new Date();
        Date date = new Date();


        boolean b = judgmentDate(date1, date);
        System.out.println(b);
    }
}


