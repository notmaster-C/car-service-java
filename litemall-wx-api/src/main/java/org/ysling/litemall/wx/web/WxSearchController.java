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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.ysling.litemall.core.utils.response.ResponseUtil;
import org.ysling.litemall.db.domain.LitemallKeyword;
import org.ysling.litemall.db.domain.LitemallSearchHistory;
import org.ysling.litemall.wx.annotation.LoginUser;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.ysling.litemall.wx.service.WxKeywordService;
import org.ysling.litemall.wx.service.WxSearchHistoryService;
import org.ysling.litemall.wx.model.search.body.SearchListBody;
import org.ysling.litemall.wx.model.search.result.SearchIndexResult;

import java.util.*;

/**
 * 商品搜索服务
 * @author Ysling
 */
@Slf4j
@RestController
@RequestMapping("/wx/search")
@Validated
public class WxSearchController {

    @Autowired
    private WxKeywordService keywordsService;
    @Autowired
    private WxSearchHistoryService searchHistoryService;

    /**
     * 搜索页面信息
     * <p>
     * 如果用户已登录，则给出用户历史搜索记录；
     * 如果没有登录，则给出空历史搜索记录。
     *
     * @param userId 用户ID，可选
     * @return 搜索页面信息
     */
    @GetMapping("index")
    public Object index(@LoginUser(require = false) String userId) {
        //取出输入框默认的关键词
        LitemallKeyword defaultKeyword = keywordsService.queryDefault();
        //取出热闹关键词
        List<LitemallKeyword> hotKeywordList = keywordsService.queryHots();
        //用户历史搜索
        List<LitemallSearchHistory> historyList = searchHistoryService.queryByUid(userId);
        //结果
        SearchIndexResult result = new SearchIndexResult();
        result.setDefaultKeyword(defaultKeyword);
        result.setHotKeywordList(hotKeywordList);
        result.setHistoryKeywordList(historyList);
        return ResponseUtil.ok(result);
    }

    /**
     * 关键字提醒
     * <p>
     * 当用户输入关键字一部分时，可以推荐系统中合适的关键字。
     */
    @GetMapping("helper")
    public Object helper(SearchListBody body) {
        List<LitemallKeyword> keywordsList = keywordsService.queryByKeyword(body);
        String[] keys = new String[keywordsList.size()];
        int index = 0;
        for (LitemallKeyword key : keywordsList) {
            keys[index++] = key.getKeyword();
        }
        return ResponseUtil.ok(keys);
    }

    /**
     * 清除用户搜索历史
     * @param userId 用户ID
     * @return 清理是否成功
     */
    @PostMapping("clear/history")
    public Object clearHistory(@LoginUser String userId) {
        searchHistoryService.deleteByUid(userId);
        return ResponseUtil.ok();
    }


}
