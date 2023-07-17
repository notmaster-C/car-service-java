package org.click.carservice.wx.model.message.body;

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
public class HistoryMessageBody extends PageBody implements Serializable {

    /**
     * 消息接收者
     */
    @NotNull(message = "消息接收者不能为空")
    private String receiveUserId;

}
