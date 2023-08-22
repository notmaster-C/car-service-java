package org.click.carservice.core.satoken.handler;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.handler.ActionLogHandler;
import org.click.carservice.core.service.CommonService;
import org.click.carservice.core.utils.bcrypt.BCryptPasswordEncoder;
import org.click.carservice.core.utils.captcha.CaptchaManager;
import org.click.carservice.core.utils.http.GlobalWebUtil;
import org.click.carservice.core.utils.ip.IpUtil;
import org.click.carservice.db.domain.CarServiceAdmin;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.service.IAdminService;
import org.click.carservice.db.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 授权登陆
 * @author Ysling
 */
@Slf4j
@Component
public class AuthenticationInfo {

    private static CommonService commonService;
    private static IAdminService adminService;

    private static IUserService userService;

    @Autowired
    public void setCommonService(CommonService commonService){
        AuthenticationInfo.commonService = commonService;
    }

    @Autowired
    public void setAdminService(IAdminService adminService){
        AuthenticationInfo.adminService = adminService;
    }

    @Autowired
    public void setUserService(IUserService userService) {
        AuthenticationInfo.userService = userService;
    }

    /**
     * 授权登陆
     * @param admin 登陆实体
     */
    public static void login(CarServiceAdmin admin) {
        String username = admin.getUsername();
        String password = admin.getPassword();
        BeanUtil.copyProperties(login(username, password), admin);
    }

    /**
     * 扫码登录授权登陆
     * @param admin 登陆实体
     */
    public static void loginQr(CarServiceAdmin admin) {
        //添加登陆记录
        admin.setLastLoginIp(IpUtil.getIpAddr(GlobalWebUtil.getRequest()));
        admin.setLastLoginTime(LocalDateTime.now());
        if (adminService.updateVersionSelective(admin) == 0){
            throw new RuntimeException("网络繁忙,请重试");
        }
        //退出登陆
        StpUtil.logout(admin.getId());
        //账号登陆
        StpUtil.login(admin.getId());
        //添加记录
        ActionLogHandler.logAuthSucceed("扫码授权登录");
    }

    /**
     * 授权登陆
     * @param admin 登陆实体
     * @param code 验证码
     */
    public static void login(CarServiceAdmin admin, String code) {
        String username = admin.getUsername();
        String password = admin.getPassword();
        BeanUtil.copyProperties(login(username, password , code), admin);
    }

    /**
     * 授权登陆
     * @param username 用户名
     * @param password 密码
     */
    public static CarServiceAdmin login(String username, String password) {
        return login(username , password , null);
    }

    /**
     * 授权登陆
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     */
    public static CarServiceAdmin login(String username, String password, String code) {
        if (!StringUtils.hasText(username)) {
            throw new RuntimeException("用户名不能为空");
        }
        if (!StringUtils.hasText(password)) {
            throw new RuntimeException("密码不能为空");
        }
        //判断验证码是否正确
        if (StringUtils.hasText(code)){
            if (CaptchaManager.isCachedCaptcha(username, code)) {
                throw new RuntimeException("验证码校验失败");
            }
        }
        //获取数据库账号消息
        List<CarServiceAdmin> adminList = commonService.findAdmin(username);
        if(adminList.size()>1){
            throw new RuntimeException(username  +"的用户数量大于一" );
        }
//        Assert.state(adminList.size() < 2, "存在两个相同账户");
        if (adminList.size() == 0) {
            throw new RuntimeException("找不到用户（" + username + "）的帐号信息");
        }
        //校验密码
        CarServiceAdmin admin = adminList.get(0);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("账号或密码错误");
        }
        //添加登录记录
        admin.setLastLoginIp(IpUtil.getIpAddr(GlobalWebUtil.getRequest()));
        admin.setLastLoginTime(LocalDateTime.now());
        if (adminService.updateVersionSelective(admin) == 0){
            throw new RuntimeException("网络繁忙,请重试");
        }
        ActionLogHandler.logAuthSucceed("账号授权登录");
        //退出登陆
        StpUtil.logout(admin.getId());
        //账号登陆
        StpUtil.login(admin.getId());
        // 判断用户是否商户，是则存储小程序商户id到session
        if (StpUtil.getRoleList().contains("商户")) {
            String mobile = admin.getMobile();
            CarServiceUser carServiceUser = userService.selectUserByMobil(mobile);
            StpUtil.getSession().set("carServiceUserId", carServiceUser.getId());
        }
        //对象拷贝
        return admin;
    }

}
