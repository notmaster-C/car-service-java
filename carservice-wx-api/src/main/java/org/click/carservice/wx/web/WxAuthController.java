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

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.annotation.JsonBody;
import org.click.carservice.core.notify.model.NotifyType;
import org.click.carservice.core.notify.service.NotifyMailService;
import org.click.carservice.core.notify.service.NotifyMobileService;
import org.click.carservice.core.redis.annotation.RequestRateLimiter;
import org.click.carservice.core.satoken.handler.AuthenticationInfo;
import org.click.carservice.core.service.CouponAssignService;
import org.click.carservice.core.utils.RandomStrUtil;
import org.click.carservice.core.utils.RegexUtil;
import org.click.carservice.core.utils.bcrypt.BCryptPasswordEncoder;
import org.click.carservice.core.utils.captcha.CaptchaManager;
import org.click.carservice.core.utils.ip.IpUtil;
import org.click.carservice.core.utils.response.ResponseStatus;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.core.utils.token.TokenManager;
import org.click.carservice.db.domain.CarServiceAdmin;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.entity.AdminInfo;
import org.click.carservice.db.entity.UserInfo;
import org.click.carservice.db.enums.UserGender;
import org.click.carservice.db.enums.UserLevel;
import org.click.carservice.db.enums.UserStatus;
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.auth.body.*;
import org.click.carservice.wx.model.auth.result.LoginQrResult;
import org.click.carservice.wx.model.auth.result.LoginResult;
import org.click.carservice.wx.model.auth.result.WxAuthResult;
import org.click.carservice.wx.model.auth.result.WxLoginResult;
import org.click.carservice.wx.service.WxAdminService;
import org.click.carservice.wx.service.WxAuthService;
import org.click.carservice.wx.service.WxUserService;
import org.redisson.api.RateIntervalUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * 鉴权服务
 *
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/auth")
@Validated
@Api(value = "微信-鉴权服务", tags = "微信-鉴权服务")
public class WxAuthController {

    @Autowired
    private WxUserService userService;
    @Autowired
    private WxAdminService adminService;
    @Autowired
    private NotifyMobileService mobileService;
    @Autowired
    private NotifyMailService mailService;
    @Autowired
    private CouponAssignService couponAssignService;
    @Autowired
    private WxAuthService wxAuthService;


    /**
     * 扫码授权登陆
     */
    @PostMapping("login_by_qr")
    @ApiOperation("扫码授权登陆")
    public ResponseUtil loginByQr(@LoginUser String userId, @Valid @RequestBody AuthLoginByQrBody body) {
        CarServiceUser user = userService.findById(userId);
        if (user == null) {
            return ResponseUtil.fail("授权失败1");
        }
        CarServiceAdmin admin = adminService.findByOpenId(user.getOpenid());
        if (admin == null) {
            return ResponseUtil.fail("授权失败2");
        }

        //授权登录
        AuthenticationInfo.loginQr(admin);

        // adminInfo
        AdminInfo adminInfo = new AdminInfo();
        BeanUtil.copyProperties(admin, adminInfo);
        // 登陆信息
        LoginQrResult result = new LoginQrResult();
        result.setAdminInfo(adminInfo);
        result.setToken(StpUtil.getTokenValue());
        result.setTenantId(TokenManager.createTenantToken(admin.getTenantId()));
        //发送socket
        wxAuthService.sendSocket(body.getAuthCode(), result);
        return ResponseUtil.ok();
    }

    /**
     * 小程序用户名密码登录
     *
     * @param body
     * @return
     */
    @PostMapping("login_by_default")
    @ApiOperation("小程序用户名密码登录")
    public ResponseUtil<WxLoginResult> login(@Valid @RequestBody AuthLoginBody body) {
        CarServiceUser user = userService.auth(body);
        UserInfo userInfo = new UserInfo();
        BeanUtil.copyProperties(user, userInfo);
        // token
        String token = TokenManager.createUserToken(user.getId());
        WxLoginResult result = new WxLoginResult();
        result.setUserToken(token);
        result.setUserInfo(userInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 账号登录
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("login")
    @ApiOperation("账号登录")
    public Object login(@LoginUser String userId, @Valid @RequestBody AuthLoginBody body) {
        String username = body.getUsername();
        String password = body.getPassword();

        //创建账户实体并登陆
        CarServiceAdmin admin = new CarServiceAdmin();
        admin.setUsername(username);
        admin.setPassword(password);
        AuthenticationInfo.login(admin, body.getCode());
        //登陆成功
        CarServiceUser user = userService.findById(userId);

        if (StringUtils.hasText(admin.getMobile())) {
            if (ObjectUtil.isNotNull(user) && !admin.getMobile().equals(user.getMobile())) {
                throw new RuntimeException("该管理员账户已绑定其他用户");
            }
        }

        //绑定新的管理员账户后，解除原有账户的绑定
        //先将原有账户解除绑定，否则findByopenid可能查出多个
        //添加事务
        CarServiceAdmin originalAdmin = adminService.findByMobile(user.getMobile());
        if (originalAdmin != null && !originalAdmin.getId().equals(admin.getId())) {
            originalAdmin.setMobile("");
            if (adminService.updateVersionSelective(originalAdmin) == 0) {
                throw new RuntimeException("网络繁忙,请重试");
            }
        }

        CarServiceAdmin service = adminService.findById(admin.getId());
        service.setMobile(user.getMobile());
        if (adminService.updateVersionSelective(service) == 0) {
            throw new RuntimeException("网络繁忙,请重试");
        }

        // adminInfo
        AdminInfo adminInfo = new AdminInfo();
        BeanUtil.copyProperties(admin, adminInfo);
        LoginResult result = new LoginResult();
        result.setAdminInfo(adminInfo);
        result.setAdminToken(StpUtil.getTokenValue());
        return ResponseUtil.ok(result);
    }

    /**
     * 请求手机验证码
     * TODO
     * 这里需要一定机制防止短信验证码被滥用
     *
     * @param mobile 手机号码
     */
    @PostMapping("captcha/mobile")
    @RequestRateLimiter(rate = 10, rateInterval = 1, timeUnit = RateIntervalUnit.DAYS, errMsg = "验证码申请超过单日限制")
    @ApiOperation("请求手机验证码")
    public Object mobileCaptcha(@LoginUser String userId, @JsonBody String mobile) {
        if (Objects.isNull(userId)) {
            return ResponseUtil.unlogin();
        }
        if (!RegexUtil.isMobileSimple(mobile)) {
            return ResponseUtil.badArgumentValue();
        }

        String code = RandomStrUtil.getRandom(6, RandomStrUtil.TYPE.NUMBER);
        if (CaptchaManager.addToCache(mobile, code)) {
            return ResponseUtil.fail("验证码未超时，不能发送");
        }

        //发送验证码到手机号
        if (mobileService.isSmsEnable()) {
            mobileService.notifySmsTemplate(mobile, NotifyType.CAPTCHA, new HashMap<String, String>() {
                {
                    put("code", code);
                }
            }.toString());
            return ResponseUtil.ok("验证码发送成功，请注意查收");
        }
//        return ResponseUtil.ok("验证码发送成功，请注意查收" + "登录验证码：" + code);
        return ResponseUtil.ok("验证码发送成功，请注意查收");
    }


    /**
     * 请求邮箱验证码
     * TODO
     * 这里需要一定机制防止短信验证码被滥用
     *
     * @param username 邮箱 { username }
     */
    @PostMapping("captcha/mail")
    @RequestRateLimiter(rate = 10, rateInterval = 1, timeUnit = RateIntervalUnit.DAYS, errMsg = "验证码申请超过单日限制")
    @ApiOperation("请求邮箱验证码")
    public Object mailCaptcha(@JsonBody String username) {
        List<CarServiceAdmin> adminList = adminService.findAdmin(username);
        if (adminList.size() != 1) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0110);
        }

        String code = RandomStrUtil.getRandom(6, RandomStrUtil.TYPE.NUMBER);
        if (CaptchaManager.addToCache(username, code)) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0242);
        }

        CarServiceAdmin admin = adminList.get(0);
        if (mailService.isMailEnable() && !RegexUtil.isQQMail(admin.getMail())) {
            mailService.notifyMail("登录验证码：" + code, "验证码30分钟内有效，如发送错误请您忽略！", admin.getMail());
            return ResponseUtil.ok("验证码发送成功，请注意查收");
        }
        return ResponseUtil.ok("验证码发送成功，请注意查收" + "登录验证码：" + code);
    }

    /**
     * 微信登录
     *
     */
    @ApiOperation("微信登录")
    @PostMapping("login_by_weixin")
    public Object loginByWeixin(@Valid @RequestBody WxLoginInfo wxLoginInfo, HttpServletRequest request) {
        String wxCode = wxLoginInfo.getWxCode();
        String phoneCode = wxLoginInfo.getPhoneCode();
        String inviter = wxLoginInfo.getInviter();
        UserInfo userInfo = wxLoginInfo.getUserInfo();
        //获取用户授权信息
        WxAuthResult wxAuth = wxAuthService.wxAuth(wxCode);
        String openId = wxAuth.getOpenId();
        String sessionKey = wxAuth.getSessionKey();
        String mobile = "";
        try {
            mobile = wxAuthService.getPhoneNumber(phoneCode);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("微信获取电话接口调用失败");
        }
        String phone = String.valueOf(request.getHeader("phone"));
        mobile = StrUtil.isBlank(mobile) ? StrUtil.isNotBlank(phone) ? phone : "17396228815" : mobile;
        userInfo.setOpenId(openId);
//        String mobile = "17396228815";
        userInfo.setOpenId(openId);
        //获取用户信息
//        CarServiceUser user = userService.queryByOid(openId);
        List<CarServiceUser> carServiceUsers = userService.queryByMobile(mobile);
        CarServiceUser user;
        if (CollUtil.isEmpty(carServiceUsers)) {
            user = new CarServiceUser();
            user.setUsername(mobile);
            user.setPassword(mobile);
            user.setOpenid(openId);
            user.setMobile(mobile);
            user.setAvatarUrl(userInfo.getAvatarUrl());
            user.setNickName(userInfo.getNickName());
            user.setGender(userInfo.getGender());
            user.setUserLevel(UserLevel.USER_LEVEL_0.getStatus());
            user.setStatus(UserStatus.STATUS_NORMAL.getStatus());
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setAddTime(LocalDateTime.now());
            user.setSessionKey(sessionKey);
            userService.add(user);
            // 新用户发送注册优惠券
//            couponAssignService.assignForRegister(user.getId());
        } else {
            user = carServiceUsers.get(0);
            if (user.getStatus().equals(UserStatus.STATUS_DISABLED.getStatus())) {
                return ResponseUtil.fail("账号被禁用");
            }
            if (user.getStatus().equals(UserStatus.STATUS_OUT.getStatus())) {
                return ResponseUtil.fail("账号已注销");
            }
            if (StringUtils.hasText(inviter) && user.getInviter() == null) {
                user.setInviter(inviter);
            }
            user.setOpenid(openId);
            user.setSessionKey(sessionKey);
            user.setLastLoginTime(LocalDateTime.now());
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            if (userService.updateVersionSelective(user) == 0) {
                return ResponseUtil.updatedDataFailed();
            }
        }

        if (StringUtils.hasText(inviter)) {
            CarServiceUser inviterUser = userService.findById(inviter);
            if (inviterUser != null) {
                Integer count = userService.countUser(inviter);
                inviterUser.setStatus(UserLevel.parseCount(count));
                if (userService.updateVersionSelective(inviterUser) == 0) {
                    throw new RuntimeException("用户信息更新失败，请重试");
                }
            }
        }

        BeanUtil.copyProperties(user, userInfo);
        // token
        String token = TokenManager.createUserToken(user.getId());
        WxLoginResult result = new WxLoginResult();
        result.setUserToken(token);
        result.setUserInfo(userInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 账号注册
     *
     * @param body    请求内容
     * @param request @Ignore
     * @return 登录结果
     */
    @ApiOperation("账号注册")
    @PostMapping("register")
    public Object register(@Valid @RequestBody AuthRegisterBody body, HttpServletRequest request) {
        String password = body.getPassword();
        String mobile = body.getMobile();
        String code = body.getCode();
        String wxCode = body.getWxCode();

//        //判断验证码是否正确
//        if (CaptchaManager.isCachedCaptcha(mobile, code)) {
//            return ResponseUtil.fail("验证码错误");
//        }

        if (!RegexUtil.isMobileSimple(mobile)) {
            return ResponseUtil.fail("手机号格式不正确");
        }

        List<CarServiceUser> userList = userService.queryByMobile(mobile);
        if (userList.size() > 0) {
            return ResponseUtil.fail("手机号已注册");
        }

        String openId = "";
        // 非空，则是小程序注册
        // 继续验证openid
        if (!Objects.isNull(wxCode)) {
            WxAuthResult wxAuth = wxAuthService.wxAuth(wxCode);
            if (wxAuth == null) {
                return ResponseUtil.fail("授权信息获取失败");
            }
            openId = wxAuth.getOpenId();
            userList = userService.queryByOpenid(openId);
            if (userList.size() > 1) {
                return ResponseUtil.serious();
            }
            if (userList.size() == 1) {
                CarServiceUser checkUser = userList.get(0);
                String checkPassword = checkUser.getPassword();
                if (!checkPassword.equals(password)
                        || !checkUser.getUsername().equals(body.getUserName())) {
                    return ResponseUtil.fail("openid已绑定账号");
                }
            }
        }

        CarServiceUser user = new CarServiceUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        // 如果没授权登录，则使用用户手机作为用户名
        user.setUsername(body.getMobile());
        user.setPassword(encodedPassword);
        user.setMobile(mobile);
        user.setOpenid(openId);
        user.setAvatarUrl("https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64");
        user.setNickName(mobile);
        user.setGender(UserGender.USER_GENDER_0.getStatus());
        user.setUserLevel(UserLevel.USER_LEVEL_0.getStatus());
        user.setStatus(UserStatus.STATUS_NORMAL.getStatus());
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.getIpAddr(request));
        user.setAddTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.add(user);

        // userInfo
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(mobile);
        userInfo.setAvatarUrl(user.getAvatarUrl());

        // token
        String token = TokenManager.createUserToken(user.getId());
        WxLoginResult result = new WxLoginResult();
        result.setUserToken(token);
        result.setUserInfo(userInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 账号密码重置
     *
     * @param body 请求内容
     * @return 登录结果
     */
    @ApiOperation("账号的-密码-重置")
    @PostMapping("reset")
    public Object reset(@Valid @RequestBody AuthResetBody body) {
        String code = body.getCode();
        String mobile = body.getMobile();
        String password = body.getPassword();

        //判断验证码是否正确
        if (CaptchaManager.isCachedCaptcha(mobile, code)) {
            return ResponseUtil.fail("验证码错误");
        }

        List<CarServiceUser> userList = userService.queryByMobile(mobile);
        if (userList.size() > 1) {
            return ResponseUtil.serious();
        } else if (userList.size() == 0) {
            return ResponseUtil.fail("手机号未注册");
        }

        CarServiceUser user = userList.get(0);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);
        if (userService.updateVersionSelective(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }

    /**
     * 账号手机号码重置
     *
     * @param userId @Ignore
     * @param body   请求内容
     */
    @Deprecated
    @ApiOperation("账号-手机号-重置")
    @PostMapping("resetPhone")
    public Object resetPhone(@LoginUser String userId, @Valid @RequestBody AuthResetBody body)  {
        String code = body.getCode();
        String mobile = body.getMobile();
        String password = body.getPassword();

        //判断验证码是否正确
        if (CaptchaManager.isCachedCaptcha(mobile, code)) {
            return ResponseUtil.fail("验证码错误");
        }

        List<CarServiceUser> userList = userService.queryByMobile(mobile);
        if (userList.size() > 1) {
            return ResponseUtil.fail("手机号已注册");
        }

        CarServiceUser user = userService.findById(userId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            return ResponseUtil.fail("账号密码不对");
        }

        user.setMobile(mobile);
        if (userService.updateVersionSelective(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }

    /**
     * 账号信息更新
     *
     * @param userId @Ignore
     * @param body   请求内容
     */
    @ApiOperation("账号信息更新")
    @PostMapping("profile")
    public Object profile(@LoginUser String userId, @Valid @RequestBody UserInfo body, HttpServletRequest request) {
        CarServiceUser user = userService.findById(userId);
        if(StringUtils.hasText(user.getMobile())){
            if(!user.getMobile().equals(body.getMobile())){
                return ResponseUtil.fail("不能修改手机号");
            }
        }
        BeanUtil.copyProperties(body, user);
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(IpUtil.getIpAddr(request));
        if (userService.updateVersionSelective(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }

    /**
     * 微信手机号码绑定
     *
     * @param userId @Ignore
     * @param body   请求内容
     */
    @PostMapping("bindPhone")
    @ApiOperation("微信手机号码绑定")
    public Object bindPhone(@LoginUser String userId, @Valid @RequestBody AuthBindPhoneBody body) {
        String iv = body.getIv();
        String encryptedData = body.getEncryptedData();
        CarServiceUser user = userService.findById(userId);
        user.setMobile(wxAuthService.getPhoneNumber(user.getSessionKey(), encryptedData, iv));
        if (userService.updateVersionSelective(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }


    /**
     * 退出登陆
     */
    @ApiOperation("退出登录")
    @PostMapping("logout")
    public Object logout() {
        return ResponseUtil.unlogin();
    }


}
