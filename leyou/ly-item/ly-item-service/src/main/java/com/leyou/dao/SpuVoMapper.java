package com.leyou.dao;

import com.leyou.vo.SpuVo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SpuVoMapper extends Mapper<SpuVo> {
    List<SpuVo> findSpuByPage(@Param("key") String key,
                              @Param("page") int page,
                              @Param("rows") int rows,
                              @Param("saleable") Integer saleable);

    Long findSpuCount(@Param("key") String key,
                      @Param("saleable") Integer saleable);

    SpuVo findSpuVoBySpuId(@Param("spuId") Long spuId);
}
