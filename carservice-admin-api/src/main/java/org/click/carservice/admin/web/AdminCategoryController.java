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
import org.click.carservice.admin.model.category.result.CategoryResult;
import org.click.carservice.admin.service.AdminCategoryService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceCategory;
import org.click.carservice.db.entity.BaseOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


/**
 * 类目管理
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/category")
@Validated
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService categoryService;

    /**
     * 查询
     */
    @SaCheckPermission("admin:category:list")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "查询")
    @GetMapping("/list")
    public Object list() {
        List<CategoryResult> categoryResultList = new ArrayList<>();
        List<carserviceCategory> categoryList = categoryService.queryByPid("0");
        for (carserviceCategory category : categoryList) {
            CategoryResult categoryResult = new CategoryResult();
            BeanUtil.copyProperties(category, categoryResult);
            List<CategoryResult> children = new ArrayList<>();
            //获取子列表
            List<carserviceCategory> subCategoryList = categoryService.queryByPid(category.getId());
            for (carserviceCategory subCategory : subCategoryList) {
                CategoryResult subCategoryVo = new CategoryResult();
                BeanUtil.copyProperties(subCategory, subCategoryVo);
                children.add(subCategoryVo);
            }
            categoryResult.setChildren(children);
            categoryResultList.add(categoryResult);
        }
        return ResponseUtil.okList(categoryResultList);
    }

    /**
     * 一级分类查询
     */
    @SaCheckPermission("admin:category:l1")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "一级分类查询")
    @GetMapping("/l1")
    public Object catL1() {
        // 所有一级分类目录
        List<carserviceCategory> l1CatList = categoryService.queryL1();
        List<BaseOption> result = new ArrayList<>(l1CatList.size());
        for (carserviceCategory category : l1CatList) {
            BaseOption option = new BaseOption();
            option.setValue(category.getId());
            option.setLabel(category.getName());
            result.add(option);
        }
        return ResponseUtil.okList(result);
    }


    /**
     * 添加分类
     */
    @SaCheckPermission("admin:category:create")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody carserviceCategory category) {
        Object error = categoryService.validate(category);
        if (error != null) {
            return error;
        }
        categoryService.add(category);
        return ResponseUtil.ok(category);
    }

    /**
     * 分类详情
     */
    @SaCheckPermission("admin:category:read")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull String id) {
        return ResponseUtil.ok(categoryService.findById(id));
    }

    /**
     * 编辑分类
     */
    @SaCheckPermission("admin:category:update")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@Valid @RequestBody carserviceCategory category) {
        Object error = categoryService.validate(category);
        if (error != null) {
            return error;
        }
        if (categoryService.updateVersionSelective(category) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }

    /**
     * 分类删除
     */
    @SaCheckPermission("admin:category:delete")
    @RequiresPermissionsDesc(menu = {"商场管理", "类目管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@NotNull String id) {
        categoryService.deleteById(id);
        return ResponseUtil.ok();
    }

}