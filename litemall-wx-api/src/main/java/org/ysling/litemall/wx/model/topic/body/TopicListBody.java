package org.ysling.litemall.wx.model.topic.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.ysling.litemall.db.entity.PageBody;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ysling
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopicListBody extends PageBody implements Serializable {

    /**
     * 专题标题
     */
    private String title;

}
