package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.click.carservice.db.domain.CarServiceOrderGoods;
import org.click.carservice.db.mapper.OrderGoodsMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.click.carservice.db.service.IOrderGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author click
 */
@Service
@Primary
@CacheConfig(cacheNames = "carservice_order_goods")
public class OrderGoodsServiceImpl extends IBaseServiceImpl<OrderGoodsMapper, CarServiceOrderGoods> implements IOrderGoodsService {

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;


    @Override
    //@Cacheable(sync = true)
    public CarServiceOrderGoods findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceOrderGoods> queryAll(Wrapper<CarServiceOrderGoods> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int add(CarServiceOrderGoods record) {
        return getBaseMapper().insert(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean batchAdd(List<CarServiceOrderGoods> list) {
        return saveBatch(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateSelective(CarServiceOrderGoods record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateVersionSelective(CarServiceOrderGoods record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean exists(Wrapper<CarServiceOrderGoods> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(CarServiceOrderGoods entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<CarServiceOrderGoods> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceOrderGoods entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceOrderGoods entity, Wrapper<CarServiceOrderGoods> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<CarServiceOrderGoods> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(CarServiceOrderGoods entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(Wrapper<CarServiceOrderGoods> queryWrapper) {
        return super.remove(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByIds(Collection<?> list, boolean useFill) {
        return super.removeByIds(list, useFill);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeBatchByIds(Collection<?> list) {
        return super.removeBatchByIds(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeBatchByIds(Collection<?> list, boolean useFill) {
        return super.removeBatchByIds(list, useFill);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateById(CarServiceOrderGoods entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(Wrapper<CarServiceOrderGoods> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(CarServiceOrderGoods entity, Wrapper<CarServiceOrderGoods> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatchById(Collection<CarServiceOrderGoods> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    //@Cacheable(sync = true)
    public CarServiceOrderGoods getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceOrderGoods> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceOrderGoods> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    //@Cacheable(sync = true)
    public CarServiceOrderGoods getOne(Wrapper<CarServiceOrderGoods> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public long count() {
        return super.count();
    }

    @Override
    //@Cacheable(sync = true)
    public long count(Wrapper<CarServiceOrderGoods> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceOrderGoods> list(Wrapper<CarServiceOrderGoods> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceOrderGoods> list() {
        return super.list();
    }

    @Override
    //@Cacheable(sync = true)
    public List<Map<String, Object>> listMaps(Wrapper<CarServiceOrderGoods> queryWrapper) {
        return super.listMaps(queryWrapper);
    }

    @Override
    public List<CarServiceOrderGoods> selectByBrandIds(List<String> ids) {
        return orderGoodsMapper.selectByBrandIds(ids);
    }
}
