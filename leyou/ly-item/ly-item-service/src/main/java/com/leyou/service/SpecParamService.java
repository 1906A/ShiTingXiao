package com.leyou.service;

import com.leyou.dao.SpecParamMapper;
import com.leyou.pojo.SpecParam;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecParamService {
    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 保存规格参数
     * @param specParam
     */
    public void saveSpecParam(SpecParam specParam) {

        specParamMapper.insertSelective(specParam);
    }

    /**
     * 根据id修改规格参数
     * @param specParam
     */
    public void updateSpecParam(SpecParam specParam) {
        specParamMapper.updateByPrimaryKeySelective(specParam);
    }
    /**
     * 根据id删除商品规格参数
     * @param id
     */
    public void delectSpecParam(Long id) {
        specParamMapper.deleteByPrimaryKey(id);
    }

    public List<SpecParam> findSpecParamByCid(Long cid) {
        SpecParam specParam = new SpecParam();
        specParam.setCid(cid);
        return specParamMapper.select(specParam);
    }

    public List<SpecParam> findSpecParamByCidAndSearching(SpecParam specParam) {
        return specParamMapper.select(specParam);
    }


    public List<SpecParam> findSpecParamByCidAndGeneric(Long cid, Integer generic) {
        return specParamMapper.findSpecParamByCidAndGeneric(cid,generic);
    }
}
