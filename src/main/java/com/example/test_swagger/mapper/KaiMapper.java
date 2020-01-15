package com.example.test_swagger.mapper;

import com.example.test_swagger.entity.TbBrand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 邵祺锴
 * @create 2020-01-15 14:04
 */
@Mapper
public interface KaiMapper {

    List<TbBrand> findAll();

    @Select("select * from tb_brand where id = #{id}")
    TbBrand findById(int id);

    @Select("select distinct name from tb_brand where id = #{id}")
    String findByName(int id);

}
