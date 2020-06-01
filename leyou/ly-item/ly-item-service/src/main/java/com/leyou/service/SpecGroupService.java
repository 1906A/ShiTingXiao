package com.leyou.service;

import com.leyou.dao.SpecGroupMapper;
import com.leyou.dao.SpecParamMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecGroupService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据cid查询商品规格组列表
     * @param cid
     * @return
     */
    public List<SpecGroup> findSpecGroupByCid(Long cid){
        return specGroupMapper.findSpecGroupByCid(cid);
    }

    /**
     * 保存商品规格组
     * @param specGroup
     */
    public void saveSpecGroup(SpecGroup specGroup) {
        specGroupMapper.insert(specGroup);
    }
    /**
     * 根据分类id删除商品规格组
     * @param id
     */
    public void delectBySpecGroupId(Long id) {
        specGroupMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据id修改商品规格组
     * @param specGroup
     */
    public void updateSpecGroup(SpecGroup specGroup) {
        specGroupMapper.updateByPrimaryKey(specGroup);
    }

    /**
     * 根据组id查询组参数
     * @param gid
     * @return
     */
    public List<SpecParam> findSpecParamByGid(Long gid) {
        SpecParam specParam = new SpecParam();
        specParam.setGroupId(gid);
        return specParamMapper.select(specParam);
    }
}
