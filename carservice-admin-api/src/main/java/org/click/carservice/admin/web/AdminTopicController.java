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

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.admin.annotation.RequiresPermissionsDesc;
import org.click.carservice.admin.model.topic.body.TopicListBody;
import org.click.carservice.admin.model.topic.result.TopicReadResult;
import org.click.carservice.admin.service.AdminGoodsService;
import org.click.carservice.admin.service.AdminTopicService;
import org.click.carservice.core.service.StorageCoreService;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.carserviceTopic;
import org.click.carservice.db.entity.IdsBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 专题管理
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/admin/topic")
@Validated
public class AdminTopicController {

    @Autowired
    private StorageCoreService storageCoreService;
    @Autowired
    private AdminTopicService topicService;
    @Autowired
    private AdminGoodsService goodsService;

    /**
     * 专题列表
     */
    @SaCheckPermission("admin:topic:list")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(TopicListBody body) {
        return ResponseUtil.okList(topicService.querySelective(body));
    }

    /**
     * 专题添加
     */
    @SaCheckPermission("admin:topic:create")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "添加")
    @PostMapping("/create")
    public Object create(@Valid @RequestBody carserviceTopic topic) {
        Object error = topicService.validate(topic);
        if (error != null) {
            return error;
        }
        String content = topic.getContent();
        String textFile = storageCoreService.uploadTextFile(content);
        topic.setContent(textFile);
        topicService.add(topic);
        return ResponseUtil.ok(topic);
    }

    /**
     * 专题详情
     * @param id 专题ID
     */
    @SaCheckPermission("admin:topic:read")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "详情")
    @GetMapping("/read")
    public Object read(@NotNull String id) {
        carserviceTopic topic = topicService.findById(id);
        TopicReadResult result = new TopicReadResult();
        result.setTopic(topic);
        result.setGoodsList(goodsService.queryByIds(topic.getGoodsIds()));
        return ResponseUtil.ok(result);
    }

    /**
     * 专题编辑
     */
    @SaCheckPermission("admin:topic:update")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@Valid @RequestBody carserviceTopic topic) {
        Object error = topicService.validate(topic);
        if (error != null) {
            return error;
        }
        String content = topic.getContent();
        String textFile = storageCoreService.uploadTextFile(content);
        topic.setContent(textFile);
        if (topicService.updateVersionSelective(topic) == 0) {
            return ResponseUtil.updatedDataFailed();
        }
        return ResponseUtil.ok(topic);
    }

    /**
     * 专题删除
     */
    @SaCheckPermission("admin:topic:delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@NotNull String id) {
        topicService.deleteById(id);
        return ResponseUtil.ok();
    }

    /**
     * 专题批量删除
     */
    @SaCheckPermission("admin:topic:batch-delete")
    @RequiresPermissionsDesc(menu = {"推广管理", "专题管理"}, button = "批量删除")
    @PostMapping("/batch-delete")
    public Object batchDelete(@Valid @RequestBody IdsBody body) {
        topicService.removeByIds(body.getIds());
        return ResponseUtil.ok();
    }


}
