package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.click.carservice.db.domain.CarServiceAfterSale;
import org.click.carservice.db.mapper.AftersaleMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.click.carservice.db.service.IAftersaleService;
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
 * 售后表 服务实现类
 * </p>
 *
 * @author click
 */
@Service
@Primary
@CacheConfig(cacheNames = "carservice_aftersale")
public class AftersaleServiceImpl extends IBaseServiceImpl<AftersaleMapper, CarServiceAfterSale> implements IAftersaleService {


    @Override
    @Cacheable(sync = true)
    public CarServiceAfterSale findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceAfterSale> queryAll(Wrapper<CarServiceAfterSale> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int add(CarServiceAfterSale record) {
        return getBaseMapper().insert(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean batchAdd(List<CarServiceAfterSale> list) {
        return saveBatch(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateSelective(CarServiceAfterSale record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateVersionSelective(CarServiceAfterSale record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean exists(Wrapper<CarServiceAfterSale> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(CarServiceAfterSale entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<CarServiceAfterSale> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceAfterSale entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceAfterSale entity, Wrapper<CarServiceAfterSale> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<CarServiceAfterSale> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(CarServiceAfterSale entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(Wrapper<CarServiceAfterSale> queryWrapper) {
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
    public boolean updateById(CarServiceAfterSale entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(Wrapper<CarServiceAfterSale> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(CarServiceAfterSale entity, Wrapper<CarServiceAfterSale> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatchById(Collection<CarServiceAfterSale> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceAfterSale getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceAfterSale> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceAfterSale> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceAfterSale getOne(Wrapper<CarServiceAfterSale> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public long count() {
        return super.count();
    }

    @Override
    @Cacheable(sync = true)
    public long count(Wrapper<CarServiceAfterSale> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceAfterSale> list(Wrapper<CarServiceAfterSale> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceAfterSale> list() {
        return super.list();
    }

    @Override
    @Cacheable(sync = true)
    public List<Map<String, Object>> listMaps(Wrapper<CarServiceAfterSale> queryWrapper) {
        return super.listMaps(queryWrapper);
    }


}
