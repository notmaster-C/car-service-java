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
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.model.notice.admin.body.NoticeAdminListBody;
import org.click.carservice.admin.model.notice.admin.result.NoticeAdminCatResult;
import org.click.carservice.admin.service.AdminAdminService;
import org.click.carservice.admin.service.AdminNoticeAdminService;
import org.click.carservice.admin.service.AdminNoticeService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceAdmin;
import org.click.carservice.db.domain.carserviceNotice;
import org.click.carservice.db.domain.carserviceNoticeAdmin;
import org.click.carservice.db.entity.IdsBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 管理员通知信息
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/profile")
@Validated
public class AdminNoticeAdminController {

    @Autowired
    private AdminAdminService adminService;
    @Autowired
    private AdminNoticeService noticeService;
    @Autowired
    private AdminNoticeAdminService noticeAdminService;

    /**
     * 通知列表
     */
    @SaCheckLogin
    @GetMapping("/notice/list")
    public Object listNotice(NoticeAdminListBody body) {
        body.setAdminId(StpUtil.getLoginIdAsString());
        return ResponseUtil.okList(noticeAdminService.querySelective(body));
    }

    /**
     * 通知信息条数
     */
    @SaCheckLogin
    @GetMapping("/notice/count")
    public Object countNotice() {
        return ResponseUtil.ok(noticeAdminService.countUnread(StpUtil.getLoginIdAsString()));
    }

    /**
     * 查看通知
     */
    @SaCheckLogin
    @PostMapping("/notice/cat")
    public Object catNotice(@NotNull String noticeId) {
        carserviceNoticeAdmin noticeAdmin = noticeAdminService.find(noticeId, StpUtil.getLoginIdAsString());
        if (noticeAdmin == null) {
            return ResponseUtil.badArgumentValue();
        }

        // 更新通知记录中的时间
        noticeAdmin.setReadTime(LocalDateTime.now());
        if (noticeAdminService.updateVersionSelective(noticeAdmin) == 0) {
            return ResponseUtil.updatedDateExpired();
        }
        carserviceNotice notice = noticeService.findById(noticeId);
        NoticeAdminCatResult result = new NoticeAdminCatResult();
        result.setTitle(notice.getTitle());
        result.setContent(notice.getContent());
        result.setTime(notice.getAddTime());
        if (notice.getAdminId().equals("0")) {
            result.setAdmin("系统");
        } else {
            carserviceAdmin admin = adminService.findById(notice.getAdminId());
            result.setAdmin(admin.getUsername());
            result.setAvatar(admin.getAvatar());
        }
        return ResponseUtil.ok(result);
    }

    /**
     * 批量浏览通知
     */
    @SaCheckLogin
    @PostMapping("/notice/batch-cat")
    public Object allCatNotice(@Valid @RequestBody IdsBody body) {
        noticeAdminService.markReadByIds(body.getIds(), StpUtil.getLoginIdAsString());
        return ResponseUtil.ok();
    }

    /**
     * 删除通知
     * @param id 通知ID
     */
    @SaCheckLogin
    @PostMapping("/notice/delete")
    public Object deleteNotice(@NotNull String id) {
        noticeAdminService.deleteById(id, StpUtil.getLoginIdAsString());
        return ResponseUtil.ok();
    }

    /**
     * 批量删除通知
     */
    @SaCheckLogin
    @PostMapping("/notice/batch-delete")
    public Object allDeleteNotice(@Valid @RequestBody IdsBody body) {
        noticeAdminService.deleteByIds(body.getIds(), StpUtil.getLoginIdAsString());
        return ResponseUtil.ok();
    }

}
