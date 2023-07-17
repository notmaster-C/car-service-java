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
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.annotation.context.PermissionsContext;
import org.click.carservice.admin.model.auth.body.CaptchaBody;
import org.click.carservice.admin.model.auth.body.LoginBody;
import org.click.carservice.admin.model.auth.result.InfoResult;
import org.click.carservice.admin.model.auth.result.LoginQrResult;
import org.click.carservice.admin.model.auth.result.LoginResult;
import org.click.carservice.admin.service.AdminAdminService;
import org.click.carservice.admin.service.AdminTenantService;
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.core.notify.service.NotifyMailService;
import org.click.carservice.core.redis.annotation.RequestRateLimiter;
import org.click.carservice.core.satoken.handler.AuthenticationInfo;
import org.click.carservice.core.service.ActionLogService;
import org.click.carservice.core.service.QrcodeCoreService;
import org.click.carservice.core.utils.RandomStrUtil;
import org.click.carservice.core.utils.RegexUtil;
import org.click.carservice.core.utils.captcha.CaptchaManager;
import org.click.carservice.core.utils.response.ResponseStatus;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.core.utils.token.TokenManager;
import org.click.carservice.db.domain.carserviceAdmin;
import org.click.carservice.db.entity.AdminInfo;
import org.redisson.api.RateIntervalUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * 授权登陆
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/auth")
@Validated
public class AdminAuthController {

    @Autowired
    private QrcodeCoreService qCodeService;
    @Autowired
    private ActionLogService logHelper;
    @Autowired
    private NotifyMailService mailService;
    @Autowired
    private AdminAdminService adminService;
    @Autowired
    private PermissionsContext permissionsContext;
    @Autowired
    private AdminTenantService tenantService;


    /**
     * 登陆验证码
     */
    @SaIgnore
    @PostMapping("/captcha")
    @RequestRateLimiter(rate = 10, rateInterval = 1, timeUnit = RateIntervalUnit.DAYS, errMsg = "验证码申请超过单日限制")
    public Object loginCaptcha(@Valid @RequestBody CaptchaBody body) {
        String username = body.getUsername();
        if (Objects.isNull(username) || username.length() < 6) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0110);
        }

        tenantService.setTenant(body.getAppid());

        List<carserviceAdmin> adminList = adminService.findAdmin(username);
        if (adminList.size() != 1) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0110);
        }

        String code = RandomStrUtil.getRandom(6, RandomStrUtil.TYPE.NUMBER);
        if (CaptchaManager.addToCache(username, code)) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0242);
        }

        carserviceAdmin admin = adminList.get(0);
        if (mailService.isMailEnable() && !RegexUtil.isQQMail(admin.getMail())) {
            mailService.notifyMail("登录验证码：" + code, "验证码30分钟内有效，如发送错误请您忽略！", admin.getMail());
            return ResponseUtil.ok("验证码发送成功，请注意查收");
        }
        return ResponseUtil.ok("验证码发送成功，请注意查收" + "登录验证码：" + code);
    }

    /**
     * 管理员登陆
     */
    @SaIgnore
    @PostMapping("/login")
    public Object login(@Valid @RequestBody LoginBody body) {
        String username = body.getUsername();
        String password = body.getPassword();
        tenantService.setTenant(body.getAppid());

        //创建账户实体并登陆
        carserviceAdmin admin = new carserviceAdmin();
        admin.setUsername(username);
        admin.setPassword(password);
        AuthenticationInfo.login(admin, body.getCode());

        // adminInfo
        AdminInfo adminInfo = new AdminInfo();
        BeanUtil.copyProperties(admin, adminInfo);
        // 登陆信息
        LoginResult result = new LoginResult();
        result.setAdminInfo(adminInfo);
        result.setToken(StpUtil.getTokenValue());
        result.setTenantId(TokenManager.createTenantToken(admin.getTenantId()));
        return ResponseUtil.ok(result);
    }

    /**
     * 登陆二维码
     */
    @SaIgnore
    @PostMapping("/login_by_qr")
    public Object loginByQr(@JsonBody(require = false) String appid) {
        tenantService.setTenant(appid);
        String page = "pages/auth/qrauth/qrauth";
        String scene = IdUtil.simpleUUID();
        //响应结果
        LoginQrResult result = new LoginQrResult();
        result.setAuthCode(scene);
        result.setAuthQrPath(qCodeService.createWxQrcode(page, scene));
        return ResponseUtil.ok(result);
    }

    /**
     * 退出登陆
     */
    @SaCheckLogin
    @PostMapping("/logout")
    public Object logout() {
        // 当前会话注销登录
        StpUtil.logout();
        //记录日志
        logHelper.logAuthSucceed("退出登录");
        return ResponseUtil.ok();
    }


    /**
     * 管理员权限信息
     */
    @SaCheckLogin
    @GetMapping("/info")
    public Object info() {
        String adminId = StpUtil.getLoginIdAsString();
        carserviceAdmin admin = adminService.findById(adminId);
        //响应结果
        InfoResult result = new InfoResult();
        result.setName(admin.getUsername());
        result.setAvatar(admin.getAvatar());
        //获取权限
        result.setRoles(StpUtil.getRoleList());
        result.setPerms(permissionsContext.toApi(StpUtil.getPermissionList()));
        return ResponseUtil.ok(result);
    }

    /**
     * 401
     */
    @SaIgnore
    @GetMapping("/401")
    public Object page401() {
        return ResponseUtil.unlogin();
    }

    /**
     * index
     */
    @GetMapping("/index")
    public Object pageIndex() {
        return ResponseUtil.ok();
    }

    /**
     * 403
     */
    @SaIgnore
    @GetMapping("/403")
    public Object page403() {
        return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0300);
    }
}
