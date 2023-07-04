package org.ysling.litemall.wx.model.comment.body;

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
public class CommentReplyListBody extends PageBody implements Serializable {

    /**
     * 评论ID
     */
    @NotNull(message = "评论ID不能为空")
    private String commentId;

    public CommentReplyListBody(String commentId){
        this.commentId = commentId;
    }

}
