package org.click.carservice.admin.model.car.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 导入的保险信息转化为用户信息
 */
@Mapper
public interface ImportInsuranceToUser {

    ImportInsuranceToUser INSTANCE = Mappers.getMapper(ImportInsuranceToUser.class);

}
