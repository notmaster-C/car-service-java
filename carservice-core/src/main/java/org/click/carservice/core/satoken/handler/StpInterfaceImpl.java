package org.click.carservice.core.satoken.handler;

import cn.dev33.satoken.stp.StpInterface;
import org.click.carservice.core.service.CommonService;
import org.click.carservice.db.domain.carserviceAdmin;
import org.click.carservice.db.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展
 * 保证此类被SpringBoot扫描，完成Sa-Token的自定义权限验证扩展
 *
 * @author click
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private CommonService commonService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        carserviceAdmin admin = adminService.findById(loginId.toString());
        if (admin == null) {
            return new ArrayList<>();
        }
        String[] roleIds = admin.getRoleIds();
        return new ArrayList<>(commonService.queryByRoleIds(roleIds));
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        carserviceAdmin admin = adminService.findById(loginId.toString());
        if (admin == null) {
            return new ArrayList<>();
        }
        String[] roleIds = admin.getRoleIds();
        return new ArrayList<>(commonService.queryByIds(roleIds));
    }


}
