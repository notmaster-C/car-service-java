package org.click.carservice.wx.model.comment.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentListBody extends PageBody implements Serializable {

    /**
     * 类型ID
     */
    @NotNull(message = "类型ID不能为空")
    private String valueId;
    /**
     * 评论类型
     */
    @NotNull(message = "评论类型不能为空")
    private Short commentType;


}
