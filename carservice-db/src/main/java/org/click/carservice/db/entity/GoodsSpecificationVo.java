package org.click.carservice.db.entity;

import lombok.Data;
import org.click.carservice.db.domain.carserviceGoodsSpecification;

import java.io.Serializable;
import java.util.List;

/**
 * @author click
 */
@Data
public class GoodsSpecificationVo implements Serializable {

    /**
     * 规格名
     */
    private String name;
    /**
     * 规格列表
     */
    private List<carserviceGoodsSpecification> valueList;


}
