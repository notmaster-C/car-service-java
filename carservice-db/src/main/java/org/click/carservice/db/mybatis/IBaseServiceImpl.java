package org.click.carservice.db.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 自定义通用接口实现
 *
 * @param <M> mapper 对象
 * @param <T> 实体类
 * @author click
 */
public class IBaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T> {


}
