package org.click.carservice.admin.model.ad.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 广告列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdListBody extends PageBody {

    /**
     * 广告名称
     */
    private String name;
    /**
     * 广告内容
     */
    private String content;

}
