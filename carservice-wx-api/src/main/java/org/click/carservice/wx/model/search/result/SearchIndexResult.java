package org.click.carservice.wx.model.search.result;

import lombok.Data;
import org.click.carservice.db.domain.carserviceKeyword;
import org.click.carservice.db.domain.carserviceSearchHistory;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class SearchIndexResult implements Serializable {


    /**
     * 取出输入框默认的关键词
     */
    private carserviceKeyword defaultKeyword;
    /**
     * 取出热闹关键词
     */
    private List<carserviceKeyword> hotKeywordList;
    /**
     * 用户历史搜索
     */
    private List<carserviceSearchHistory> historyKeywordList;


}
