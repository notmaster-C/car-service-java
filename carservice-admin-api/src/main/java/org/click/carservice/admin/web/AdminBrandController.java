package org.click.carservice.admin.web;
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

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.annotation.RequiresPermissionsDesc;
import org.click.carservice.admin.model.brand.body.BrandListBody;
import org.click.carservice.admin.model.brand.body.BrandSaveBody;
import org.click.carservice.admin.service.AdminBrandService;
import org.click.carservice.admin.service.AdminGoodsService;
import org.click.carservice.core.service.GoodsCoreService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceBrand;
import org.click.carservice.db.domain.carserviceGoods;
import org.click.carservice.db.enums.BrandStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 品牌管理
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/brand")
@Validated
public class AdminBrandController {

    @Autowired
    private AdminBrandService brandService;
    @Autowired
    private AdminGoodsService goodsService;
    @Autowired
    private GoodsCoreService goodsCoreService;

    /**
     * 查询
     */
    @SaCheckPermission("admin:brand:list")
    @RequiresPermissionsDesc(menu = {"商场管理", "品牌管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(BrandListBody body) {
        return ResponseUtil.okList(brandService.querySelective(body));
    }


    /**
     * 详情
     */
    @SaCheckPermission("admin:brand:read")
    @RequiresPermissionsDesc(menu = {"商场管理", "品牌管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull String id) {
        return ResponseUtil.ok(brandService.findById(id));
    }

    /**
     * 添加
     */
    @SaCheckPermission("admin:brand:create")
    @RequiresPermissionsDesc(menu = {"商场管理", "品牌管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody BrandSaveBody body) {
        carserviceBrand brand = new carserviceBrand();
        BeanUtil.copyProperties(body, brand);
        Object error = brandService.validate(brand);
        if (error != null) {
            return error;
        }
        brandService.add(brand);
        return ResponseUtil.ok(brand);
    }

    /**
     * 编辑
     */
    @SaCheckPermission("admin:brand:update")
    @RequiresPermissionsDesc(menu = {"商场管理", "品牌管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@Valid @RequestBody carserviceBrand brand) {
        Object error = brandService.validate(brand);
        if (error != null) {
            return error;
        }
        //禁用店铺删除所有商品
        if (!brand.getStatus().equals(BrandStatus.STATUS_NORMAL.getStatus())) {
            List<carserviceGoods> goodsList = goodsService.queryByBrand(brand.getId());
            for (carserviceGoods goods : goodsList) {
                goodsCoreService.goodsDelete(goods);
            }
        }
        if (brandService.updateVersionSelective(brand) == 0) {
            throw new RuntimeException("店铺更新失败");
        }
        return ResponseUtil.ok(brand);
    }

    /**
     * 删除
     */
    @SaCheckPermission("admin:brand:delete")
    @RequiresPermissionsDesc(menu = {"商场管理", "品牌管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@NotNull String id) {
        if (brandService.deleteById(id) == 0) {
            return ResponseUtil.fail("删除失败请重试");
        }
        List<carserviceGoods> goodsList = goodsService.queryByBrand(id);
        for (carserviceGoods goods : goodsList) {
            goodsCoreService.goodsDelete(goods);
        }
        return ResponseUtil.ok();
    }

}
