package com.leyou.dao;

import com.leyou.pojo.SpecGroup;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpecGroupMapper extends Mapper<SpecGroup> {
    List<SpecGroup> findSpecGroupByCid(Long cid);
}
