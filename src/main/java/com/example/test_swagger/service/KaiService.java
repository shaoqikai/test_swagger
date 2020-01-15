package com.example.test_swagger.service;

import com.example.test_swagger.entity.TbBrand;
import com.example.test_swagger.mapper.KaiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 邵祺锴
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

}
