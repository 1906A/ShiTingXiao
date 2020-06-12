package com.leyou.service;

import com.leyou.common.PageResult;
import com.leyou.dao.*;
import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;
import com.leyou.pojo.Stock;
import com.leyou.vo.SpuVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SpuService {
    @Autowired
    private SpuVoMapper spuVoMapper;
    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    //引入消息am
    @Autowired
    AmqpTemplate amqpTemplate;

    /**
     * 根据分页信息查询商品参数列表
     *
     * @param key
     * @param page
     * @param rows
     * @param saleable
     * @return
     */
    public PageResult<SpuVo> findSpuByPage(String key, Integer page, Integer rows, Integer saleable) {
        /**
         * 1: 分页公式+商品信息 (page-1)*rows,rows
         * 2:总条数
         * */
        PageResult<SpuVo> result = findSpuBySql(key, page, rows, saleable);
        return result;
    }

    public PageResult<SpuVo> findSpuBySql(String key, Integer page, Integer rows, Integer saleable) {
        List<SpuVo> spuList = spuVoMapper.findSpuByPage(key, (page - 1) * rows, rows, saleable);
        // 2:总条数
        Long count = spuVoMapper.findSpuCount(key, saleable);
        //有参构造
        PageResult<SpuVo> result = new PageResult<SpuVo>(count, spuList);
        return result;
    }

    public void addGoods(SpuVo spuVo) {
        /**
         * 1: 新增spu表
         * 2: 新增sku表
         * 3: 新增spuDetail表
         * 4: 新增stock表
         * */
        Date nowTime = new Date();
        // 1: 新增spu表
        SpuVo spu = new SpuVo();
        spu.setBrandId(spuVo.getBrandId());
        spu.setCid1(spuVo.getCid1());
        spu.setCid2(spuVo.getCid2());
        spu.setCid3(spuVo.getCid3());
        spu.setSubTitle(spuVo.getSubTitle());
        spu.setTitle(spuVo.getTitle());
        spu.setSaleable(false);
        spu.setValid(true);
        spu.setCreateTime(nowTime);
        spu.setLastUpdateTime(nowTime);
        spuMapper.insert(spu);

        //2: 新增spuDetail表
        SpuDetail spuDetail = spuVo.getSpuDetail();
        spuDetail.setSpuId(spu.getId());
        spuDetailMapper.insert(spuDetail);

        //3: 新增sku表
        List<Sku> skus = spuVo.getSkus();
        skus.forEach(sku -> {
            sku.setSpuId(spu.getId());
            sku.setEnable(true);
            sku.setCreateTime(nowTime);
            sku.setLastUpdateTime(nowTime);
            skuMapper.insert(sku);

            //库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
        });
        //调用发送消息的方法
        this.sendMQ("insert",spu.getId());
    }

    /**
     * 发送MQ消息的方法
     *
     * @param type
     * @param spuId
     */
    public void sendMQ(String type, Long spuId) {
        System.out.println("进入调用");
        //发送mq消息
        amqpTemplate.convertAndSend("item.exchanges", "item."+type, spuId);
        System.out.println("调用完成");
    }


    public SpuDetail findSpuDetailBySpuId(Long id) {
        return spuDetailMapper.selectByPrimaryKey(id);
    }

    public void updateGoods(SpuVo spuVo) {
        /**
         * 1: 修改spu表
         * 2: 修改sku表- - 先删除,后添加 以为一对多,如果对比修改,逻辑麻烦
         * 3: 修改spuDetail表
         * 4: 修改stock表 先删除 后添加 以为一对多,如果对比修改,逻辑麻烦
         * */
        // 1: 修改spu表
        Date lastUpdateTime = new Date();
        spuVo.setLastUpdateTime(lastUpdateTime);
        spuMapper.updateByPrimaryKeySelective(spuVo);

        //2: 修改spuDetail表 一对一
        SpuDetail spuDetail = spuVo.getSpuDetail();
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);

        // 3: 修改sku表- - 先删除,后添加 以为一对多,如果对比修改,逻辑麻烦
       /* List<Sku> skus = spuVo.getSkus();
        skus.forEach(sku -> {
            //或者对应spuID,然后逻辑删除对应spuID的所有sku
            sku.setEnable(false);
            skuMapper.updateByPrimaryKeySelective(sku);
            //skuMapper.updateByExampleSelective(sku,spuVo.getId());
            //删除库存stock
            stockMapper.deleteByPrimaryKey(sku.getId());
        });*/

       List<Sku> skuList = skuMapper.findSkuBySpuId(spuVo.getId());
       skuList.forEach(s->{
           skuMapper.deleteByPrimaryKey(s.getId());
           stockMapper.deleteByPrimaryKey(s.getId());
       });

        //新增sku
        List<Sku> skus1 = spuVo.getSkus();
        skus1.forEach(sku -> {
            sku.setSpuId(spuVo.getId());
            sku.setEnable(true);
            sku.setCreateTime(lastUpdateTime);
            sku.setLastUpdateTime(lastUpdateTime);
            skuMapper.insert(sku);

            //库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            stockMapper.insert(stock);
        });
        //调用发送消息的方法
        System.out.println(spuVo.getId()+"================");
        this.sendMQ("update",spuVo.getId());
    }

    public void deleteById(Long spuId) {
        /**
         * 1: 修改spu表
         * 2: 修改spuDetail表
         * 3: 修改sku表- - 先删除,后添加 以为一对多,如果对比修改,逻辑麻烦
         * 4: 修改stock表 先删除 后添加 以为一对多,如果对比修改,逻辑麻烦
         *
         * 为了防止出现脏数据(也就是垃圾无用的数据遗留数据库),类似多表删除,都是倒序删除
         * */
        //1: 删除stock表
        List<Sku> skuList = skuMapper.findSkuBySpuId(spuId);
        skuList.forEach(sku -> {
            //或者对应spuID,然后逻辑删除对应spuID的所有sku
            sku.setEnable(false);
            skuMapper.updateByPrimaryKeySelective(sku);
            //skuMapper.updateByExampleSelective(sku,spuVo.getId());
            //删除库存stock
            stockMapper.deleteByPrimaryKey(sku.getId());
        });
        //2: 删除spuDetail表
        spuDetailMapper.deleteByPrimaryKey(spuId);

        //3: 删除spu表
        SpuVo spu = new SpuVo();
        //逻辑删除
        spu.setValid(false);
        spu.setId(spuId);
        spuMapper.updateByPrimaryKeySelective(spu);

        //发送删除商品消息的MQ
        this.sendMQ("delete",spuId);
    }

    public void upOrDown(Boolean saleable, Long spuId) {
        SpuVo spu = new SpuVo();
        spu.setId(spuId);
        spu.setSaleable(saleable);
        spuMapper.updateByPrimaryKeySelective(spu);
    }

    /**
     * 根据spuId查询spu信息
     *
     * @param spuId
     * @return
     */
    public Spu findSpuBySpuId(Long spuId) {
        return spuMapper.selectByPrimaryKey(spuId);
    }

    public SpuVo findSpuVoBySpuId(Long spuId) {
       return spuVoMapper.findSpuVoBySpuId(spuId);
    }
}
