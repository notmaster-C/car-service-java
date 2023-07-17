package org.click.carservice.wx.web;
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

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.core.service.GoodsCoreService;
import org.click.carservice.db.domain.CarServiceGoods;
import org.click.carservice.db.entity.GoodsAllinone;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.brand.body.BrandListBody;
import org.click.carservice.wx.model.brand.body.BrandOrderListBody;
import org.click.carservice.wx.model.brand.body.BrandSaveBody;
import org.click.carservice.wx.model.brand.result.BrandGoodsListBody;
import org.click.carservice.wx.web.impl.WxWebBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * 店铺信息
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/brand")
@Validated
public class WxBrandController {

    @Autowired
    private WxWebBrandService brandService;
    @Autowired
    private GoodsCoreService goodsCoreService;


    /**
     * 店铺列表
     */
    @GetMapping("list")
    public Object list(BrandListBody body) {
        return brandService.list(body);
    }


    /**
     * 店铺详情
     * @param brandId 品牌ID
     * @return 品牌详情
     */
    @GetMapping("read")
    public Object read(@LoginUser String userId, @JsonBody String brandId) {
        return brandService.read(userId , brandId);
    }

    /**
     * 店铺详情
     * @param brandId 品牌ID
     * @return 品牌详情
     */
    @GetMapping("detail")
    public Object brandDetail(@LoginUser String userId ,@JsonBody String brandId) {
        return brandService.brandDetail(userId , brandId);
    }


    /**
     * 添加或修改店铺
     * @param userId    用户ID
     * @param body     店铺信息
     * @return 成功
     */
    @PostMapping("/save")
    public Object brandSave(@LoginUser String userId, @Valid @RequestBody BrandSaveBody body) {
        return brandService.brandSave(userId , body);
    }

    /**
     * 店铺订单列表
     */
    @GetMapping("/order")
    public Object orderList(@LoginUser String userId, BrandOrderListBody body) {
        return brandService.orderList(userId , body);
    }

    /**
     * 商品上传参数初始化
     * @return 分类
     */
    @GetMapping("/goods/init")
    public Object goodsInit() {
        return brandService.goodsInit();
    }

    /**
     * 分类列表
     * @return 分类
     */
    @GetMapping("/goods/category")
    public Object catList() {
        return brandService.catList();
    }

    /**
     * 店铺商品列表
     */
    @GetMapping("goods/list")
    public Object goodsList(BrandGoodsListBody body) {
        return brandService.goodsList(body);
    }

    /**
     * 店铺商品详情
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/goods/detail")
    public Object goodsDetail(@NotNull String id) {
        return brandService.goodsDetail(id);
    }

    /**
     * 编辑店铺商品
     * @param goodsAllinone 商品信息
     */
    @PostMapping("/goods/update")
    public Object goodsUpdate(@Valid @RequestBody GoodsAllinone goodsAllinone) {
        return goodsCoreService.goodsUpdate(goodsAllinone);
    }

    /**
     * 删除店铺商品
     * @param goods 商品信息
     * @return 成功
     */
    @PostMapping("/goods/delete")
    public Object goodsDelete(@Valid @RequestBody CarServiceGoods goods) {
        return goodsCoreService.goodsDelete(goods);
    }

    /**
     * 添加店铺商品
     * @param goodsAllinone 商品信息
     * @return 成功
     */
    @PostMapping("/goods/create")
    public Object goodsCreate(@Valid @RequestBody GoodsAllinone goodsAllinone) {
        return goodsCoreService.goodsCreate(goodsAllinone);
    }



}