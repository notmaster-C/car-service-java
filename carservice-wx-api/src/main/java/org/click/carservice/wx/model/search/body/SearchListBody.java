package org.click.carservice.wx.model.search.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SearchListBody extends PageBody implements Serializable {

    /**
     * 关键字
     */
    @NotEmpty
    private String keyword;

}
