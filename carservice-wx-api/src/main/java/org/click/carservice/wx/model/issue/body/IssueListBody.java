package org.click.carservice.wx.model.issue.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import java.io.Serializable;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class IssueListBody extends PageBody implements Serializable {

    /**
     * 问题标题
     */
    private String question;

}
