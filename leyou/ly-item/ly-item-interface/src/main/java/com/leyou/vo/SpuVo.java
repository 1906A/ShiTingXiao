package com.leyou.vo;

import com.leyou.pojo.Sku;
import com.leyou.pojo.Spu;
import com.leyou.pojo.SpuDetail;

import java.util.List;

public class SpuVo extends Spu {
   /* spu
    brandId: 1528
    cid1: 74
    cid2: 75
    cid3: 76
    subTitle: "11"
    title: "11"

    spuDetail
    afterService: "11"
    description: "<p>11<img src="http://image.leyou.com/group1/M00/00/00/wKg6hF7HAj6EC5JRAAAAACqwOg4294.jpg"></p>"
    genericSpec: "{"1":"11","2":"11","3":"11","5":"11","6":"11","7":"11","8":"11","9":"11","10":"11","11":"11","14":"11","15":"11","16":"11","17":"11","18":"11"}"
    packingList: "11"
    specialSpec: "{"4":["红"],"12":["1"],"13":["12"]}"

    skus
    enable: true
    images: "http://image.leyou.com/group1/M00/00/00/wKg6hF7HApuEdPIBAAAAACqwOg4027.jpg"
    indexes: "0_0_0"
    ownSpec: "{"4":"红","12":"1","13":"12"}"
    price: 1100
    stock: "11"
    title: "11 红 1 12"*/

   private SpuDetail spuDetail;
   private List<Sku> skus;

    public SpuDetail getSpuDetail() {
        return spuDetail;
    }

    public void setSpuDetail(SpuDetail spuDetail) {
        this.spuDetail = spuDetail;
    }

    public List<Sku> getSkus() {
        return skus;
    }

    public void setSkus(List<Sku> skus) {
        this.skus = skus;
    }
}
