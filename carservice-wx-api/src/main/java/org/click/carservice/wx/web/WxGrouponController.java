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
import lombok.extern.slf4j.Slf4j;
import org.click.carservice.db.entity.PageBody;
import org.click.carservice.wx.web.impl.WxWebGrouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.NotNull;


/**
 * 团购服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/groupon")
@Validated
public class WxGrouponController {


    @Autowired
    private WxWebGrouponService grouponService;


    /**
     * 团购规则列表
     */
    @GetMapping("list")
    public Object list(PageBody body) {
        return grouponService.list(body);
    }

    /**
     * 参加团购
     * @param grouponId 团购活动ID
     * @return 操作结果
     */
    @GetMapping("join")
    public Object join(@NotNull String grouponId) {
        return grouponService.join(grouponId);
    }

}
