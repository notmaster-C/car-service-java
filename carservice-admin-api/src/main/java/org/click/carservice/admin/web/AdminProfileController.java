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

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.click.carservice.admin.model.profile.body.ProfilePasswordBody;
import org.click.carservice.admin.model.profile.result.AdminUpdateBody;
import org.click.carservice.admin.service.AdminAdminService;
import org.click.carservice.admin.service.AdminTenantService;
import org.click.carservice.core.utils.bcrypt.BCryptPasswordEncoder;
import org.click.carservice.core.utils.response.ResponseStatus;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceAdmin;
import org.click.carservice.db.domain.carserviceTenant;
import org.click.carservice.db.entity.AdminInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

/**
 * 管理员个人账号管理
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/profile")
@Validated
public class AdminProfileController {

    @Autowired
    private AdminAdminService adminService;
    @Autowired
    private AdminTenantService tenantService;

    /**
     * 修改密码
     */
    @SaCheckLogin
    @PostMapping("/password")
    public Object password(@Valid @RequestBody ProfilePasswordBody body) {
        String oldPassword = body.getOldPassword();
        String newPassword = body.getNewPassword();
        String confirmPassword = body.getConfirmPassword();

        if (!ObjectUtils.allNotNull(oldPassword, newPassword)) {
            return ResponseUtil.badArgument();
        }

        if (newPassword == null || newPassword.length() < 6) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0121);
        }

        if (!Objects.equals(newPassword, confirmPassword)) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0134);
        }

        carserviceAdmin admin = adminService.findById(StpUtil.getLoginIdAsString());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, admin.getPassword())) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0210);
        }

        admin.setPassword(encoder.encode(newPassword));
        if (adminService.updateVersionSelective(admin) == 0) {
            throw new RuntimeException("网络繁忙,请重试");
        }
        return ResponseUtil.ok();
    }


    /**
     * 账号详情
     */
    @SaCheckLogin
    @GetMapping("/detail")
    public Object detail() {
        carserviceAdmin admin = adminService.findById(StpUtil.getLoginIdAsString());
        AdminInfo info = new AdminInfo();
        BeanUtil.copyProperties(admin, info);
        carserviceTenant tenant = tenantService.findById(admin.getTenantId());
        info.setTenant(tenant != null ? tenant.getAddress() : "平台管理员");
        return ResponseUtil.ok(info);
    }


    /**
     * 更新账号信息
     */
    @SaCheckLogin
    @PostMapping("/update")
    public Object update(@Valid @RequestBody AdminUpdateBody body) {
        carserviceAdmin admin = new carserviceAdmin();
        admin.setId(StpUtil.getLoginIdAsString());
        BeanUtil.copyProperties(body, admin);
        if (adminService.updateVersionSelective(admin) == 0) {
            throw new RuntimeException("网络繁忙,请重试");
        }
        return ResponseUtil.ok();
    }

}
