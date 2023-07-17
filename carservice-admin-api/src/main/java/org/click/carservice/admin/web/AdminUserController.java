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
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.annotation.RequiresPermissionsDesc;
import org.click.carservice.admin.model.user.body.DealingSlipListBody;
import org.click.carservice.admin.model.user.body.UserListBody;
import org.click.carservice.admin.service.AdminDealingSlipService;
import org.click.carservice.admin.service.AdminUserService;
import org.click.carservice.core.service.DealingSlipCoreService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * 用户管理
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/user")
@Validated
public class AdminUserController {

    @Autowired
    private AdminUserService userService;
    @Autowired
    private DealingSlipCoreService dealingSlipCoreService;
    @Autowired
    private AdminDealingSlipService dealingSlipService;


    /**
     * 用户查询
     */
    @SaCheckPermission("admin:user:list")
    @RequiresPermissionsDesc(menu = {"用户管理", "会员管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(UserListBody body) {
        return ResponseUtil.okList(userService.querySelective(body));
    }

    /**
     * 用户详情
     * @param id 用户ID
     */
    @SaCheckPermission("admin:user:detail")
    @RequiresPermissionsDesc(menu = {"用户管理", "会员管理"}, button = "详情")
    @GetMapping("/detail")
    public Object userDetail(@NotNull String id) {
        return ResponseUtil.ok(userService.findById(id));
    }

    /**
     * 用户编辑
     */
    @SaCheckPermission("admin:user:update")
    @RequiresPermissionsDesc(menu = {"用户管理", "会员管理"}, button = "编辑")
    @PostMapping("/update")
    public Object userUpdate(@Valid @RequestBody carserviceUser user) {
        //判断是否修改余额，如果是添加修改记录
        dealingSlipCoreService.systemIntegralUpdate(user);
        return ResponseUtil.ok();
    }

    /**
     * 用户批量上传
     */
    @SaCheckPermission("admin:user:upload")
    @RequiresPermissionsDesc(menu = {"用户管理", "会员管理"}, button = "用户上传")
    @PostMapping("/upload")
    public Object create(@RequestParam("file") MultipartFile file) throws IOException {
        ExcelReader reader = ExcelUtil.getReader(file.getInputStream());
        List<carserviceUser> userList = reader.readAll(carserviceUser.class);
        for (carserviceUser user : userList) {
            if (user.getId() == null) {
                userService.add(user);
            } else {
                carserviceUser service = userService.findById(user.getId());
                if (service == null) {
                    userService.add(user);
                } else {
                    if (userService.updateSelective(user) == 0) {
                        return ResponseUtil.fail(String.format("用户ID(%s)更新失败", user.getId()));
                    }
                }
            }
        }
        return ResponseUtil.ok();
    }

    /**
     * 用户交易记录
     */
    @SaCheckPermission("admin:user:deal-list")
    @RequiresPermissionsDesc(menu = {"用户管理", "会员管理"}, button = "交易记录")
    @GetMapping("/deal-list")
    public Object tradingRecord(DealingSlipListBody body) {
        if (Objects.isNull(body.getUserId())) {
            return ResponseUtil.unlogin();
        }
        carserviceUser user = userService.findById(body.getUserId());
        if (user == null) {
            return ResponseUtil.fail("用户不存在");
        }
        return ResponseUtil.okList(dealingSlipService.querySelective(body));
    }


}
