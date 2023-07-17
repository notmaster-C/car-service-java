package org.click.carservice.wx.model.cart.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class CartCheckedBody implements Serializable {

    /**
     * 是否选中
     */
    @NotNull(message = "是否选中不能为空")
    private Integer isChecked;
    /**
     * 货品Ids
     */
    @NotNull(message = "货品Ids不能为空")
    private List<String> productIds;

}
