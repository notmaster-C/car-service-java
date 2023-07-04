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
import lombok.extern.slf4j.Slf4j;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.ysling.litemall.admin.annotation.RequiresPermissionsDesc;
import org.ysling.litemall.admin.service.AdminSystemService;
import org.ysling.litemall.core.system.SystemConfig;
import org.ysling.litemall.core.utils.JacksonUtil;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 系统配置
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/admin/config")
@Validated
public class AdminConfigController {

    @Autowired
    private AdminSystemService systemService;

    /**
     * 商场配置详情
     */
    @SaCheckPermission("admin:config:mall:list")
    @RequiresPermissionsDesc(menu = {"配置管理", "商场配置"}, button = "详情")
    @GetMapping("/mall")
    public Object listMall() {
        return ResponseUtil.ok(systemService.listMail());
    }

    /**
     * 商场配置编辑
     */
    @SaCheckPermission("admin:config:mall:updateConfigs")
    @RequiresPermissionsDesc(menu = {"配置管理", "商场配置"}, button = "编辑")
    @PostMapping("/mall")
    public Object updateMall(@Valid @RequestBody String body) {
        Map<String, String> data = JacksonUtil.toMap(body);
        if (data == null){
            return ResponseUtil.badArgument();
        }
        systemService.updateConfig(data);
        SystemConfig.updateConfigs(data);
        return ResponseUtil.ok();
    }

    /**
     * 运费配置详情
     */
    @SaCheckPermission("admin:config:express:list")
    @RequiresPermissionsDesc(menu = {"配置管理", "运费配置"}, button = "详情")
    @GetMapping("/express")
    public Object listExpress() {
        return ResponseUtil.ok(systemService.listExpress());
    }

    /**
     * 运费配置编辑
     */
    @SaCheckPermission("admin:config:express:updateConfigs")
    @RequiresPermissionsDesc(menu = {"配置管理", "运费配置"}, button = "编辑")
    @PostMapping("/express")
    public Object updateExpress(@Valid @RequestBody String body) {
        Map<String, String> data = JacksonUtil.toMap(body);
        if (data == null){
            return ResponseUtil.badArgument();
        }
        systemService.updateConfig(data);
        SystemConfig.updateConfigs(data);
        return ResponseUtil.ok();
    }

    /**
     * 订单配置详情
     */
    @SaCheckPermission("admin:config:order:list")
    @RequiresPermissionsDesc(menu = {"配置管理", "订单配置"}, button = "详情")
    @GetMapping("/order")
    public Object lisOrder() {
        return ResponseUtil.ok(systemService.listOrder());
    }


    /**
     * 订单配置编辑
     */
    @SaCheckPermission("admin:config:order:updateConfigs")
    @RequiresPermissionsDesc(menu = {"配置管理", "订单配置"}, button = "编辑")
    @PostMapping("/order")
    public Object updateOrder(@Valid @RequestBody String body) {
        Map<String, String> data = JacksonUtil.toMap(body);
        if (data == null){
            return ResponseUtil.badArgument();
        }
        systemService.updateConfig(data);
        SystemConfig.updateConfigs(data);
        return ResponseUtil.ok();
    }

    /**
     * 小程序配置详情
     */
    @SaCheckPermission("admin:config:wx:list")
    @RequiresPermissionsDesc(menu = {"配置管理", "小程序配置"}, button = "详情")
    @GetMapping("/wx")
    public Object listWx() {
        return ResponseUtil.ok(systemService.listWx());
    }

    /**
     * 小程序配置编辑
     */
    @SaCheckPermission("admin:config:wx:updateConfigs")
    @RequiresPermissionsDesc(menu = {"配置管理", "小程序配置"}, button = "编辑")
    @PostMapping("/wx")
    public Object updateWx(@Valid @RequestBody String body) {
        Map<String, String> data = JacksonUtil.toMap(body);
        if (data == null){
            return ResponseUtil.badArgument();
        }
        systemService.updateConfig(data);
        SystemConfig.updateConfigs(data);
        return ResponseUtil.ok();
    }


}
