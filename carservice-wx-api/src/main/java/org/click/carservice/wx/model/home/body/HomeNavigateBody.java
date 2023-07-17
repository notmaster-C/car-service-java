package org.click.carservice.wx.model.home.body;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author click
 */
@Data
public class HomeNavigateBody implements Serializable {


    /**
     * 分享ID
     */
    @NotNull(message = "分享ID不能为空")
    private String sceneId;

    /**
     * 分享类型
     */
    @NotNull(message = "分享类型不能为空")
    private String sceneType;


}
