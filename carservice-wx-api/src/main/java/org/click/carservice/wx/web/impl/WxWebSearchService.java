package org.click.carservice.wx.web.impl;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.utils.response.ResponseUtil;
import org.click.carservice.db.domain.CarServiceKeyword;
import org.click.carservice.db.domain.CarServiceSearchHistory;
import org.click.carservice.wx.model.search.body.SearchListBody;
import org.click.carservice.wx.model.search.result.SearchIndexResult;
import org.click.carservice.wx.service.WxKeywordService;
import org.click.carservice.wx.service.WxSearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商品搜索服务
 * @author Ysling
 */
@Slf4j
@Service
public class WxWebSearchService {

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
    public Object index(String userId) {
        //取出输入框默认的关键词
        CarServiceKeyword defaultKeyword = keywordsService.queryDefault();
        //取出热闹关键词
        List<CarServiceKeyword> hotKeywordList = keywordsService.queryHots();
        //用户历史搜索
        List<CarServiceSearchHistory> historyList = searchHistoryService.queryByUid(userId);
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
    public Object helper(SearchListBody body) {
        List<CarServiceKeyword> keywordsList = keywordsService.queryByKeyword(body);
        String[] keys = new String[keywordsList.size()];
        int index = 0;
        for (CarServiceKeyword key : keywordsList) {
            keys[index++] = key.getKeyword();
        }
        return ResponseUtil.ok(keys);
    }

    /**
     * 清除用户搜索历史
     * @param userId 用户ID
     * @return 清理是否成功
     */
    public Object clearHistory(String userId) {
        searchHistoryService.deleteByUid(userId);
        return ResponseUtil.ok();
    }


}
