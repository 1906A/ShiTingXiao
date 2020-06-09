package com.leyou.service;

import com.leyou.dao.SpecGroupMapper;
import com.leyou.dao.SpecParamMapper;
import com.leyou.pojo.SpecGroup;
import com.leyou.pojo.SpecParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SpecGroupService {
    @Autowired
    private SpecGroupMapper specGroupMapper;
    @Autowired
    private SpecParamMapper specParamMapper;

    /**
     * 根据分类cid查询商品规格组列表
     *
     * @param cid
     * @return
     */
    public List<SpecGroup> findSpecGroupByCid(Long cid){
        //构建查询条件
        SpecGroup specGroup = new SpecGroup();
        specGroup.setCid(cid);

        //获取查询结果
        List<SpecGroup> specGroupList = new ArrayList<>();
        specGroupList = specGroupMapper.select(specGroup);

        //查询参数组,以及参数组下面的所有信息
        specGroupList.forEach(group->{
            SpecParam param = new SpecParam();
            param.setGroupId(group.getId());
            List<SpecParam> specParamList = specParamMapper.select(param);
            group.setParams(specParamList);
        });

        return specGroupList;
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
