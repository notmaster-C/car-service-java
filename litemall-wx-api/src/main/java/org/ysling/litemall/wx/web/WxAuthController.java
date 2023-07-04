package org.ysling.litemall.wx.web;
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

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RateIntervalUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.ysling.litemall.core.annotation.JsonBody;
import org.ysling.litemall.core.notify.service.NotifyMailService;
import org.ysling.litemall.core.notify.model.NotifyType;
import org.ysling.litemall.core.notify.service.NotifyMobileService;
import org.ysling.litemall.core.redis.annotation.RequestRateLimiter;
import org.ysling.litemall.core.satoken.handler.AuthenticationInfo;
import org.ysling.litemall.core.service.ActionLogService;
import org.ysling.litemall.core.service.CouponAssignService;
import org.ysling.litemall.core.utils.*;
import org.ysling.litemall.core.utils.bcrypt.BCryptPasswordEncoder;
import org.ysling.litemall.core.utils.captcha.CaptchaManager;
import org.ysling.litemall.core.utils.ip.IpUtil;
import org.ysling.litemall.core.utils.response.ResponseStatus;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.core.utils.token.TokenManager;
import org.ysling.litemall.db.domain.LitemallAdmin;
import org.ysling.litemall.db.domain.LitemallUser;
import org.ysling.litemall.db.entity.AdminInfo;
import org.ysling.litemall.db.enums.UserGender;
import org.ysling.litemall.db.enums.UserStatus;
import org.ysling.litemall.db.enums.UserLevel;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.ysling.litemall.db.entity.UserInfo;
import org.ysling.litemall.wx.model.auth.body.WxLoginInfo;
import org.ysling.litemall.wx.service.WxAdminService;
import org.ysling.litemall.wx.service.WxUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.wx.model.auth.body.*;
import org.ysling.litemall.wx.model.auth.result.LoginQrResult;
import org.ysling.litemall.wx.model.auth.result.LoginResult;
import org.ysling.litemall.wx.model.auth.result.WxAuthResult;
import org.ysling.litemall.wx.model.auth.result.WxLoginResult;
import org.ysling.litemall.wx.service.WxAuthService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 鉴权服务
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/auth")
@Validated
public class WxAuthController {


    @Autowired
    private ActionLogService logHelper;
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
    public Object loginByQr(@LoginUser String userId, @Valid @RequestBody AuthLoginByQrBody body) {
        LitemallUser user = userService.findById(userId);
        if (user == null){
            return ResponseUtil.fail("授权失败1");
        }
        LitemallAdmin admin = adminService.findByOpenId(user.getOpenid());
        if (admin == null){
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
     * 账号登录
     */
    @PostMapping("login")
    public Object login(@LoginUser String userId, @Valid @RequestBody AuthLoginBody body) {
        String username = body.getUsername();
        String password = body.getPassword();

        //创建账户实体并登陆
        LitemallAdmin admin = new LitemallAdmin();
        admin.setUsername(username);
        admin.setPassword(password);
        AuthenticationInfo.login(admin, body.getCode());
        //登陆成功
        LitemallUser user = userService.findById(userId);
        if (StringUtils.hasText(admin.getOpenid())){
            if (!admin.getOpenid().equals(user.getOpenid())){
                throw new RuntimeException("该管理员账户已绑定其他用户");
            }
        } else {
            LitemallAdmin service = adminService.findById(admin.getId());
            service.setOpenid(user.getOpenid());
            if (adminService.updateVersionSelective(service) == 0){
                throw new RuntimeException("网络繁忙,请重试");
            }
        }
        //绑定新的管理员账户后，解除原有账户的绑定
        LitemallAdmin originalAdmin = adminService.findByOpenId(user.getOpenid());
        if (originalAdmin != null && !originalAdmin.getId().equals(admin.getId())){
            originalAdmin.setOpenid("");
            if (adminService.updateVersionSelective(originalAdmin) == 0){
                throw new RuntimeException("网络繁忙,请重试");
            }
        }

        // adminInfo
        AdminInfo adminInfo = new AdminInfo();
        BeanUtil.copyProperties(admin , adminInfo);
        LoginResult result = new LoginResult();
        result.setAdminInfo(adminInfo);
        result.setAdminToken(StpUtil.getTokenValue());
        return ResponseUtil.ok(result);
    }

    /**
     * 请求手机验证码
     * TODO
     * 这里需要一定机制防止短信验证码被滥用
     * @param mobile 手机号码
     */
    @PostMapping("captcha/mobile")
    @RequestRateLimiter(rate = 10, rateInterval = 1, timeUnit = RateIntervalUnit.DAYS , errMsg = "验证码申请超过单日限制")
    public Object mobileCaptcha(@LoginUser String userId, @JsonBody String mobile) {
        if(Objects.isNull(userId)){
            return ResponseUtil.unlogin();
        }
        if (!RegexUtil.isMobileSimple(mobile)) {
            return ResponseUtil.badArgumentValue();
        }

        String code = RandomStrUtil.getRandom(6, RandomStrUtil.TYPE.NUMBER);;
        if (CaptchaManager.addToCache(mobile, code)) {
            return ResponseUtil.fail("验证码未超时，不能发送");
        }

        //发送验证码到手机号
        if (mobileService.isSmsEnable()){
            mobileService.notifySmsTemplate(mobile, NotifyType.CAPTCHA, new String[]{code});
            return ResponseUtil.ok("验证码发送成功，请注意查收");
        }
        return ResponseUtil.ok("验证码发送成功，请注意查收" + "登录验证码："+code);
    }


    /**
     * 请求邮箱验证码
     * TODO
     * 这里需要一定机制防止短信验证码被滥用
     * @param username 邮箱 { username }
     */
    @PostMapping("captcha/mail")
    @RequestRateLimiter(rate = 10, rateInterval = 1, timeUnit = RateIntervalUnit.DAYS , errMsg = "验证码申请超过单日限制")
    public Object mailCaptcha(@JsonBody String username) {
        List<LitemallAdmin> adminList = adminService.findAdmin(username);
        if (adminList.size() != 1){
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0110);
        }

        String code = RandomStrUtil.getRandom(6, RandomStrUtil.TYPE.NUMBER);
        if (CaptchaManager.addToCache(username, code)) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0242);
        }

        LitemallAdmin admin = adminList.get(0);
        if (mailService.isMailEnable() && !RegexUtil.isQQMail(admin.getMail())){
            mailService.notifyMail("登录验证码："+code, "验证码30分钟内有效，如发送错误请您忽略！", admin.getMail());
            return ResponseUtil.ok("验证码发送成功，请注意查收");
        }
        return ResponseUtil.ok("验证码发送成功，请注意查收" + "登录验证码："+code);
    }

    /**
     * 微信登录
     */
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
//        String mobile = wxAuthService.getPhoneNumber(phoneCode);
        String mobile = "17396228815";
        userInfo.setOpenId(openId);
        //获取用户信息
        LitemallUser user = userService.queryByOid(openId);
        if (user == null) {
            user = new LitemallUser();
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
            user.setSessionKey(sessionKey);
            userService.add(user);
            // 新用户发送注册优惠券
            couponAssignService.assignForRegister(user.getId());
        } else {
            if (user.getStatus().equals(UserStatus.STATUS_DISABLED.getStatus())){
                return ResponseUtil.fail("账号被禁用");
            }
            if (user.getStatus().equals(UserStatus.STATUS_OUT.getStatus())){
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

        if (StringUtils.hasText(inviter)){
            LitemallUser inviterUser = userService.findById(inviter);
            if (inviterUser != null){
                Integer count = userService.countUser(inviter);
                inviterUser.setStatus(UserLevel.parseCount(count));
                if (userService.updateVersionSelective(inviterUser) == 0){
                    throw new RuntimeException("用户信息更新失败，请重试");
                }
            }
        }

        BeanUtil.copyProperties(user , userInfo);
        // token
        String token = TokenManager.createUserToken(user.getId());
        WxLoginResult result = new WxLoginResult();
        result.setUserToken(token);
        result.setUserInfo(userInfo);
        return ResponseUtil.ok(result);
    }

    /**
     * 账号注册
     * @param body    请求内容
     * @param request @Ignore
     * @return 登录结果
     */
    @PostMapping("register")
    public Object register(@Valid @RequestBody AuthRegisterBody body, HttpServletRequest request){
        String password = body.getPassword();
        String mobile = body.getMobile();
        String code = body.getCode();
        String wxCode = body.getWxCode();

        List<LitemallUser> userList = userService.queryByMobile(mobile);
        if (userList.size() > 0) {
            return ResponseUtil.fail("用户名已注册");
        }

        userList = userService.queryByMobile(mobile);
        if (userList.size() > 0) {
            return ResponseUtil.fail( "手机号已注册");
        }
        if (!RegexUtil.isMobileSimple(mobile)) {
            return ResponseUtil.fail( "手机号格式不正确");
        }

        //判断验证码是否正确
        if (CaptchaManager.isCachedCaptcha(mobile,code)) {
            return ResponseUtil.fail( "验证码错误");
        }

        String openId = "";
        // 非空，则是小程序注册
        // 继续验证openid
        if(!Objects.isNull(wxCode)) {
            WxAuthResult wxAuth = wxAuthService.wxAuth(wxCode);
            if (wxAuth == null){
                return ResponseUtil.fail( "授权信息获取失败");
            }
            openId = wxAuth.getOpenId();
            userList = userService.queryByOpenid(openId);
            if (userList.size() > 1) {
                return ResponseUtil.serious();
            }
            if (userList.size() == 1) {
                LitemallUser checkUser = userList.get(0);
                String checkPassword = checkUser.getPassword();
                if (!checkPassword.equals(openId)) {
                    return ResponseUtil.fail( "openid已绑定账号");
                }
            }
        }

        LitemallUser user = new LitemallUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(password);
        user.setUsername(openId);
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
     * @param body    请求内容
     * @return 登录结果
     */
    @PostMapping("reset")
    public Object reset(@Valid @RequestBody AuthResetBody body){
        String code = body.getCode();
        String mobile = body.getMobile();
        String password = body.getPassword();

        //判断验证码是否正确
        if (CaptchaManager.isCachedCaptcha(mobile,code)) {
            return ResponseUtil.fail( "验证码错误");
        }

        List<LitemallUser> userList = userService.queryByMobile(mobile);
        if (userList.size() > 1) {
            return ResponseUtil.serious();
        } else if (userList.size() == 0) {
            return ResponseUtil.fail( "手机号未注册");
        }

        LitemallUser user = userList.get(0);
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
     * @param userId  @Ignore
     * @param body   请求内容
     */
    @PostMapping("resetPhone")
    public Object resetPhone(@LoginUser String userId, @Valid @RequestBody AuthResetBody body){
        String code = body.getCode();
        String mobile = body.getMobile();
        String password = body.getPassword();

        //判断验证码是否正确
        if (CaptchaManager.isCachedCaptcha(mobile,code)) {
            return ResponseUtil.fail( "验证码错误");
        }

        List<LitemallUser> userList = userService.queryByMobile(mobile);
        if (userList.size() > 1) {
            return ResponseUtil.fail( "手机号已注册");
        }

        LitemallUser user = userService.findById(userId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, user.getPassword())) {
            return ResponseUtil.fail( "账号密码不对");
        }

        user.setMobile(mobile);
        if (userService.updateVersionSelective(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }

    /**
     * 账号信息更新
     * @param userId  @Ignore
     * @param body    请求内容
     */
    @PostMapping("profile")
    public Object profile(@LoginUser String userId, @Valid @RequestBody UserInfo body , HttpServletRequest request){
        LitemallUser user = userService.findById(userId);
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
     * @param userId  @Ignore
     * @param body    请求内容
     */
    @PostMapping("bindPhone")
    public Object bindPhone(@LoginUser String userId, @Valid @RequestBody AuthBindPhoneBody body){
        String iv = body.getIv();
        String encryptedData = body.getEncryptedData();
        LitemallUser user = userService.findById(userId);
        user.setMobile(wxAuthService.getPhoneNumber(user.getSessionKey(),encryptedData,iv));
        if (userService.updateVersionSelective(user) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok();
    }


    /**
     * 退出登陆
     */
    @PostMapping("logout")
    public Object logout() {
        return ResponseUtil.unlogin();
    }


}
