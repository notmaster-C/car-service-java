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
import org.click.carservice.wx.annotation.LoginUser;
import org.click.carservice.wx.model.search.body.SearchListBody;
import org.click.carservice.wx.web.impl.WxWebSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品搜索服务
 * @author click
 */
@Slf4j
@RestController
@RequestMapping("/wx/search")
@Validated
public class WxSearchController {

    @Autowired
    private WxWebSearchService searchService;

    /**
     * 搜索页面信息
     * @param userId 用户ID，可选
     * @return 搜索页面信息
     */
    @GetMapping("index")
    public Object index(@LoginUser(require = false) String userId) {
        return searchService.index(userId);
    }

    /**
     * 关键字提醒
     * <p>
     * 当用户输入关键字一部分时，可以推荐系统中合适的关键字。
     */
    @GetMapping("helper")
    public Object helper(SearchListBody body) {
        return searchService.helper(body);
    }

    /**
     * 清除用户搜索历史
     * @param userId 用户ID
     * @return 清理是否成功
     */
    @PostMapping("clear/history")
    public Object clearHistory(@LoginUser String userId) {
        return searchService.clearHistory(userId);
    }


}
