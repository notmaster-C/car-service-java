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
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.ysling.litemall.admin.annotation.RequiresPermissionsDesc;
import org.ysling.litemall.admin.model.user.body.DealingSlipListBody;
import org.ysling.litemall.admin.model.user.body.UserListBody;
import org.ysling.litemall.core.service.DealingSlipCoreService;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.domain.LitemallUser;
import org.ysling.litemall.admin.service.AdminDealingSlipService;
import org.ysling.litemall.admin.service.AdminUserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 用户管理
 * @author Ysling
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
    public Object userUpdate(@Valid @RequestBody LitemallUser user) {
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
        List<LitemallUser> userList = reader.readAll(LitemallUser.class);
        for (LitemallUser user :userList) {
            if (user.getId() == null){
                userService.add(user);
            } else {
                LitemallUser service = userService.findById(user.getId());
                if (service == null){
                    userService.add(user);
                } else {
                    if (userService.updateSelective(user) == 0){
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
        LitemallUser user = userService.findById(body.getUserId());
        if (user == null) {
            return ResponseUtil.fail("用户不存在");
        }
        return ResponseUtil.okList(dealingSlipService.querySelective(body));
    }


}
