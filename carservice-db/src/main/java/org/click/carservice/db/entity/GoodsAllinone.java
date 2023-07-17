package org.click.carservice.db.entity;
/**
 * Copyright (c) [click] [927069313@qq.com]
 * [carservice-plus] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import lombok.Data;
import org.click.carservice.db.domain.*;

import java.io.Serializable;

/**
 * @author click
 */
@Data
public class GoodsAllinone implements Serializable {

    /**
     * 商品基本信息
     */
    private carserviceGoods goods;
    /**
     * 货品信息
     */
    private carserviceGoodsProduct[] products;
    /**
     * 团购规则
     */
    private carserviceGrouponRules grouponRules;
    /**
     * 商品参数
     */
    private carserviceGoodsAttribute[] attributes;
    /**
     * 商品规格
     */
    private carserviceGoodsSpecification[] specifications;

}
