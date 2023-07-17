package org.click.carservice.admin.model.topic.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * 专题列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopicListBody extends PageBody implements Serializable {

    /**
     * 标题
     */
    private String title;
    /**
     * 子标题
     */
    private String subtitle;

}
