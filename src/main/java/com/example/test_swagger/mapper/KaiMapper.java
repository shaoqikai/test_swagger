package com.example.test_swagger.mapper;

import com.example.test_swagger.entity.SysUser;
import com.example.test_swagger.entity.TbBrand;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author shaoqk
 * @create 2020-01-15 14:04
 */
@Mapper
public interface KaiMapper {

    List<TbBrand> findAll();

    @Select("select * from tb_brand where id = #{id}")
    TbBrand findById(int id);

    @Select("select distinct name from tb_brand where id = #{id}")
    String findByName(int id);

    SysUser selectById(String openid);

    /**
     * 添加用户接口
     * @param user
     */
    void insertUser(SysUser user);

    void updateUser(SysUser user);
}
