package org.click.carservice.wx.model.topic.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopicListBody extends PageBody implements Serializable {

    /**
     * 专题标题
     */
    private String title;

}
