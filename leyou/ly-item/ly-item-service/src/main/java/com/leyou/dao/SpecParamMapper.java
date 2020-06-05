package com.leyou.dao;

import com.leyou.pojo.SpecParam;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecParamMapper extends Mapper<SpecParam> {
    @Select("SELECT * FROM `tb_spec_param` WHERE cid =#{cid} and generic=#{generic}")
    List<SpecParam> findSpecParamByCidAndGeneric(Long cid, Integer generic);
}
