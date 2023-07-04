package org.ysling.litemall.admin.web;
/**
 *  Copyright (c) [ysling] [927069313@qq.com]
 *  [litemall-plus] is licensed under Mulan PSL v2.
 *  You can use this software according to the terms and conditions of the Mulan PSL v2.
 *  You may obtain a copy of Mulan PSL v2 at:
 *              http://license.coscl.org.cn/MulanPSL2
 *  THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 *  EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 *  MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *  See the Mulan PSL v2 for more details.
 */

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.admin.annotation.RequiresPermissionsDesc;
import org.ysling.litemall.admin.model.dynamic.body.DynamicListBody;
import org.ysling.litemall.admin.model.dynamic.result.DynamicListResult;
import org.ysling.litemall.core.jobs.ApiJob;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.domain.LitemallDynamic;
import org.ysling.litemall.db.domain.LitemallUser;
import org.ysling.litemall.admin.service.AdminDynamicService;
import org.ysling.litemall.admin.service.AdminUserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 动态信息发布
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/admin/dynamic")
@Validated
public class AdminDynamicController {

    @Autowired
    private AdminUserService userService;
    @Autowired
    private AdminDynamicService dynamicService;


    /**
     * 动态列表
     */
    @SaCheckPermission("admin:dynamic:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "动态管理"}, button = "查询")
    @GetMapping("/list")
    public Object dynamicList(DynamicListBody body) {
        List<LitemallDynamic> dynamicList = dynamicService.querySelective(body);
        ArrayList<DynamicListResult> resultList = new ArrayList<>();
        for (LitemallDynamic dynamic : dynamicList) {
            DynamicListResult result = new DynamicListResult();
            BeanUtil.copyProperties(dynamic , result);
            //给查寻出来的时间加上浏览量
            LitemallUser user = userService.findById(dynamic.getUserId());
            if (user == null){
                if (ApiJob.USER_ID.equals(dynamic.getUserId())){
                    result.setNickName("每日段子");
                    result.setAvatarUrl("https://th.bing.com/th?id=OSK.2fe5b3f3f141834f896fe8a9ffe3a1dc&w=148&h=148&c=7&o=6&dpr=1.8&pid=SANGAM");
                } else {
                    continue;
                }
            } else {
                result.setNickName(user.getNickName());
                result.setAvatarUrl(user.getAvatarUrl());
            }
            resultList.add(result);
        }
        return ResponseUtil.okList(resultList, dynamicList);
    }


    /**
     * 修改日常
     */
    @SaCheckPermission("admin:dynamic:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "动态管理"}, button = "修改")
    @PostMapping("/update")
    public Object update(@Valid @RequestBody LitemallDynamic dynamic) {
        String content = dynamic.getContent();
        if (Objects.isNull(content)) {
            return ResponseUtil.badArgument();
        }
        dynamicService.updateSelective(dynamic);
        return ResponseUtil.ok();
    }

    /**
     * 发布日常
     */
    @SaCheckPermission("admin:dynamic:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "动态管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody LitemallDynamic dynamic) {
        String content = dynamic.getContent();
        if (Objects.isNull(content)) {
            return ResponseUtil.badArgument();
        }
        dynamicService.add(dynamic);
        return ResponseUtil.ok();
    }

    /**
     * 删除日常
     * @param id 动态发布信息的id
     */
    @SaCheckPermission("admin:dynamic:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "动态管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@NotNull String id) {
        dynamicService.deleteById(id);
        return ResponseUtil.ok();
    }


}
