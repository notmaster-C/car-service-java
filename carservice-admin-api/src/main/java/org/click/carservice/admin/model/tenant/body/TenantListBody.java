package org.click.carservice.admin.model.tenant.body;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.click.carservice.db.entity.PageBody;

/**
 * 多租户列表请求参数
 *
 * @author click
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TenantListBody extends PageBody {


    /**
     * 租户名称
     */
    private String address;


}
