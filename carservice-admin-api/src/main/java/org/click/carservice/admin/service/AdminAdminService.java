package org.click.carservice.admin.service;
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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.click.carservice.admin.model.admin.body.AdminListBody;
import org.click.carservice.core.utils.RegexUtil;
import org.click.carservice.core.utils.bcrypt.BCryptPasswordEncoder;
import org.click.carservice.core.utils.response.ResponseStatus;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceAdmin;
import org.click.carservice.db.domain.CarServiceUser;
import org.click.carservice.db.enums.UserGender;
import org.click.carservice.db.enums.UserLevel;
import org.click.carservice.db.enums.UserStatus;
import org.click.carservice.db.service.IUserService;
import org.click.carservice.db.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


/**
 * 管理员服务
 *
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_admin")
public class AdminAdminService extends AdminServiceImpl {

    @Autowired
    IUserService userService;

    public Object validate(CarServiceAdmin admin) {
        String name = admin.getUsername();
        if (Objects.isNull(name) || name.length() < 6) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0110);
        }
        if (RegexUtil.isQQMail(admin.getMail())) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0153);
        }
        if (!RegexUtil.isMobileSimple(admin.getMobile())) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0151);
        }
        String password = admin.getPassword();
        if (Objects.isNull(password) || password.length() < 6) {
            return ResponseUtil.fail(ResponseStatus.USER_ERROR_A0122);
        }
        return null;
    }

    @CacheEvict(allEntries = true)
    public boolean saveAdmin(CarServiceAdmin admin) {
        String rawPassword = admin.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        admin.setPassword(encodedPassword);
        return saveOrUpdate(admin);
    }

    @Cacheable(sync = true)
    public List<CarServiceAdmin> findAdmin(String username) {
        QueryWrapper<CarServiceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAdmin.USERNAME, username);
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public List<CarServiceAdmin> findByTenantId(String tenantId) {
        QueryWrapper<CarServiceAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceAdmin.TENANT_ID, tenantId);
        return queryAll(wrapper);
    }

    @Cacheable(sync = true)
    public List<CarServiceAdmin> querySelective(AdminListBody body) {
        QueryWrapper<CarServiceAdmin> wrapper = startPage(body);
        if (StringUtils.hasText(body.getMail())) {
            wrapper.like(CarServiceAdmin.MAIL, body.getMail());
        }
        if (StringUtils.hasText(body.getMobile())) {
            wrapper.like(CarServiceAdmin.MOBILE, body.getMobile());
        }
        if (StringUtils.hasText(body.getUsername())) {
            wrapper.like(CarServiceAdmin.USERNAME, body.getUsername());
        }
        return queryAll(wrapper);
    }


    /**
     * 通过admin账号添加wx前台账号
     *
     * @param admin
     * @return
     */
    public CarServiceUser addWxByAdmin(CarServiceAdmin admin) {
        if (!RegexUtil.isMobileSimple(admin.getMobile())) {
            throw new RuntimeException("手机号格式不正确");
        }
        List<CarServiceUser> userList = this.queryByMobile(admin.getMobile());
        if (userList.size() > 0) {
            throw new RuntimeException("手机号已注册");
        }
//        if (admin.getOpenid() != null && !admin.getOpenid().isEmpty()) {
//            userList = this.queryByOpenid(admin.getOpenid());
//            if (userList.size() > 1) {
//                throw new RuntimeException("openid已被使用");
//            } else if (userList.size() == 1) {
//                CarServiceUser checkUser = userList.get(0);
//                String checkPassword = checkUser.getPassword();
//                if (!checkUser.getUsername().equals(admin.getUsername())
//                        || !checkPassword.equals(admin.getPassword())) {
//                    throw new RuntimeException("openid已绑定账号");
//                }
//            }
//        }
        CarServiceUser user = new CarServiceUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(admin.getPassword());
        // 使用用户手机作为用户名
        user.setUsername(admin.getMobile());
        user.setPassword(encodedPassword);
        user.setUserType(1);
        user.setMobile(admin.getMobile());
        user.setOpenid(admin.getOpenid());
        //todo 为商家创建的账号的头像设置
        user.setAvatarUrl("https://yanxuan.nosdn.127.net/80841d741d7fa3073e0ae27bf487339f.jpg?imageView&quality=90&thumbnail=64x64");
        user.setNickName(admin.getMobile());
        user.setGender(UserGender.USER_GENDER_0.getStatus());
        user.setUserLevel(UserLevel.USER_LEVEL_0.getStatus());
        user.setStatus(UserStatus.STATUS_NORMAL.getStatus());
//        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp("");
        user.setUpdateTime(LocalDateTime.now());
        user.setAddTime(LocalDateTime.now());
        userService.add(user);
        return user;
    }

    private List<CarServiceUser> queryByMobile(String mobile) {
        QueryWrapper<CarServiceUser> wrapper = new QueryWrapper<>();
        wrapper.eq(CarServiceUser.MOBILE, mobile);
        return userService.queryAll(wrapper);
    }


}
