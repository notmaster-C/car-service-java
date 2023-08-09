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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "微信-店铺", tags = "微信-店铺")
public class WxBrandController {

    @Autowired
    private WxWebBrandService brandService;
    @Autowired
    private GoodsCoreService goodsCoreService;


    /**
     * 店铺列表
     */
    @GetMapping("list")
    @ApiOperation(value = "分页排序列表")
    public Object list(BrandListBody body) {
        return brandService.list(body);
    }


    /**
     * 店铺详情
     * @param brandId 品牌ID
     * @return 品牌详情
     */
    @GetMapping("read")
    @ApiOperation(value = "用户id,品牌id,品牌信息")
    public Object read(@LoginUser String userId, @JsonBody String brandId) {
        return brandService.read(userId , brandId);
    }

    /**
     * 店铺详情
     * @param brandId 品牌ID
     * @return 品牌详情
     */
    @GetMapping("detail")
    @ApiOperation(value = "用户id,品牌id,品牌详情")
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
    @ApiOperation(value = "添加或修改店铺")
    public Object brandSave(@LoginUser String userId, @Valid @RequestBody BrandSaveBody body) {
        return brandService.brandSave(userId , body);
    }

    /**
     * 店铺订单列表
     */
    @GetMapping("/order")
    @ApiOperation(value = "店铺订单列表")
    public Object orderList(@LoginUser String userId, BrandOrderListBody body) {
        return brandService.orderList(userId , body);
    }

    /**
     * 商品上传参数初始化
     * @return 分类
     */
    @GetMapping("/goods/init")
    @ApiOperation(value = "商品上传参数初始化")
    public Object goodsInit() {
        return brandService.goodsInit();
    }

    /**
     * 分类列表
     * @return 分类
     */
    @GetMapping("/goods/category")
    @ApiOperation(value = "分类列表")
    public Object catList() {
        return brandService.catList();
    }

    /**
     * 店铺商品列表
     */
    @GetMapping("goods/list")
    @ApiOperation(value = "店铺商品列表")
    public Object goodsList(BrandGoodsListBody body) {
        return brandService.goodsList(body);
    }

    /**
     * 店铺商品详情
     * @param id 商品ID
     * @return 商品信息
     */
    @GetMapping("/goods/detail")
    @ApiOperation(value = "店铺商品详情")
    public Object goodsDetail(@NotNull String id) {
        return brandService.goodsDetail(id);
    }

    /**
     * 编辑店铺商品
     * @param goodsAllinone 商品信息
     */
    @PostMapping("/goods/update")
    @ApiOperation(value = "编辑店铺商品")
    public Object goodsUpdate(@Valid @RequestBody GoodsAllinone goodsAllinone) {
        return goodsCoreService.goodsUpdate(goodsAllinone);
    }

    /**
     * 删除店铺商品
     * @param goods 商品信息
     * @return 成功
     */
    @PostMapping("/goods/delete")
    @ApiOperation(value = "删除店铺商品")
    public Object goodsDelete(@Valid @RequestBody CarServiceGoods goods) {
        return goodsCoreService.goodsDelete(goods);
    }

    /**
     * 添加店铺商品
     * @param goodsAllinone 商品信息
     * @return 成功
     */
    @PostMapping("/goods/create")
    @ApiOperation(value = "添加店铺商品")
    public Object goodsCreate(@Valid @RequestBody GoodsAllinone goodsAllinone) {
        return goodsCoreService.goodsCreate(goodsAllinone);
    }



}