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
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.click.carservice.admin.model.notice.admin.body.NoticeAdminListBody;
import org.click.carservice.db.domain.carserviceNoticeAdmin;
import org.click.carservice.db.service.impl.NoticeAdminServiceImpl;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 通知管理员服务
 * @author click
 */
@Service
@CacheConfig(cacheNames = "carservice_noticeAdmin")
public class AdminNoticeAdminService extends NoticeAdminServiceImpl {


    @Cacheable(sync = true)
    public List<carserviceNoticeAdmin> querySelective(NoticeAdminListBody body) {
        QueryWrapper<carserviceNoticeAdmin> wrapper = startPage(body);
        wrapper.eq(carserviceNoticeAdmin.ADMIN_ID, body.getAdminId());
        if ("read".equals(body.getAdminId())) {
            wrapper.isNotNull(carserviceNoticeAdmin.READ_TIME);
        }
        if ("unread".equals(body.getAdminId())) {
            wrapper.isNull(carserviceNoticeAdmin.READ_TIME);
        }
        if (StringUtils.hasText(body.getTitle())) {
            wrapper.like(carserviceNoticeAdmin.NOTICE_TITLE, body.getTitle());
        }
        return queryAll(wrapper);
    }


    @Cacheable(sync = true)
    public carserviceNoticeAdmin find(String noticeId, String adminId) {
        QueryWrapper<carserviceNoticeAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceNoticeAdmin.NOTICE_ID, noticeId);
        wrapper.eq(carserviceNoticeAdmin.ADMIN_ID, adminId);
        return getOne(wrapper);
    }


    @CacheEvict(allEntries = true)
    public void markReadByIds(List<String> ids, String adminId) {
        UpdateWrapper<carserviceNoticeAdmin> wrapper = new UpdateWrapper<>();
        wrapper.in(carserviceNoticeAdmin.ID, ids);
        wrapper.eq(carserviceNoticeAdmin.ADMIN_ID, adminId);
        carserviceNoticeAdmin noticeAdmin = new carserviceNoticeAdmin();
        LocalDateTime now = LocalDateTime.now();
        noticeAdmin.setReadTime(now);
        noticeAdmin.setUpdateTime(now);
        update(noticeAdmin, wrapper);
    }


    @CacheEvict(allEntries = true)
    public void deleteById(String id, String adminId) {
        UpdateWrapper<carserviceNoticeAdmin> wrapper = new UpdateWrapper<>();
        wrapper.eq(carserviceNoticeAdmin.ID, id);
        wrapper.eq(carserviceNoticeAdmin.ADMIN_ID, adminId);
        carserviceNoticeAdmin noticeAdmin = new carserviceNoticeAdmin();
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdmin.setDeleted(true);
        update(noticeAdmin, wrapper);
    }


    @CacheEvict(allEntries = true)
    public void deleteByIds(List<String> ids, String adminId) {
        UpdateWrapper<carserviceNoticeAdmin> wrapper = new UpdateWrapper<>();
        wrapper.in(carserviceNoticeAdmin.ID, ids);
        wrapper.eq(carserviceNoticeAdmin.ADMIN_ID, adminId);
        remove(wrapper);
    }


    @Cacheable(sync = true)
    public Integer countUnread(String adminId) {
        QueryWrapper<carserviceNoticeAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceNoticeAdmin.ADMIN_ID, adminId);
        wrapper.isNull(carserviceNoticeAdmin.READ_TIME);
        return Math.toIntExact(count(wrapper));
    }


    @Cacheable(sync = true)
    public List<carserviceNoticeAdmin> queryByNoticeId(String noticeId) {
        QueryWrapper<carserviceNoticeAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceNoticeAdmin.NOTICE_ID, noticeId);
        return queryAll(wrapper);
    }


    @CacheEvict(allEntries = true)
    public void deleteByNoticeId(String id) {
        UpdateWrapper<carserviceNoticeAdmin> wrapper = new UpdateWrapper<>();
        wrapper.eq(carserviceNoticeAdmin.NOTICE_ID, id);
        remove(wrapper);
    }


    @CacheEvict(allEntries = true)
    public void deleteByNoticeIds(List<String> ids) {
        UpdateWrapper<carserviceNoticeAdmin> wrapper = new UpdateWrapper<>();
        wrapper.in(carserviceNoticeAdmin.NOTICE_ID, ids);
        remove(wrapper);
    }


    @Cacheable(sync = true)
    public Integer countReadByNoticeId(String noticeId) {
        QueryWrapper<carserviceNoticeAdmin> wrapper = new QueryWrapper<>();
        wrapper.eq(carserviceNoticeAdmin.NOTICE_ID, noticeId);
        wrapper.isNotNull(carserviceNoticeAdmin.READ_TIME);
        return Math.toIntExact(count(wrapper));
    }


    @CacheEvict(allEntries = true)
    public void updateByNoticeId(carserviceNoticeAdmin noticeAdmin, String noticeId) {
        UpdateWrapper<carserviceNoticeAdmin> wrapper = new UpdateWrapper<>();
        wrapper.eq(carserviceNoticeAdmin.NOTICE_ID, noticeId);
        update(noticeAdmin, wrapper);
    }


}
