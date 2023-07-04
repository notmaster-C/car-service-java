package org.ysling.litemall.wx.model.collect.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Ysling
 */
@Data
public class CollectUpdateBody implements Serializable {

    /**
     * 收藏类型
     */
    @NotNull(message = "收藏类型不能为空")
    private Byte type;
    /**
     * 收藏类型ID
     */
    @NotNull(message = "收藏类型ID不能为空")
    private String valueId;

}
