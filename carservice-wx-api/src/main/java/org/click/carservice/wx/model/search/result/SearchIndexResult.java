package org.click.carservice.wx.model.search.result;

import lombok.Data;
import org.click.carservice.db.domain.CarServiceKeyword;
import org.click.carservice.db.domain.CarServiceSearchHistory;

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
    private CarServiceKeyword defaultKeyword;
    /**
     * 取出热闹关键词
     */
    private List<CarServiceKeyword> hotKeywordList;
    /**
     * 用户历史搜索
     */
    private List<CarServiceSearchHistory> historyKeywordList;


}
