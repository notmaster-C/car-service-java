package org.click.carservice.db.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * id列表请求参数
 *
 * @author click
 */
@Data
public class IdsBody implements Serializable {

    /**
     * id列表
     */
    @NotNull
    private List<String> ids;


}
