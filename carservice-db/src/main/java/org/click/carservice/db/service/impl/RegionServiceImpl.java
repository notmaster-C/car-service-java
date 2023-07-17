package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.click.carservice.db.domain.CarServiceRegion;
import org.click.carservice.db.mapper.RegionMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.click.carservice.db.service.IRegionService;
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
 * 行政区域表 服务实现类
 * </p>
 *
 * @author click
 */
@Service
@Primary
@CacheConfig(cacheNames = "carservice_region")
public class RegionServiceImpl extends IBaseServiceImpl<RegionMapper, CarServiceRegion> implements IRegionService {


    @Override
    @Cacheable(sync = true)
    public CarServiceRegion findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceRegion> queryAll(Wrapper<CarServiceRegion> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int add(CarServiceRegion record) {
        return getBaseMapper().insert(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean batchAdd(List<CarServiceRegion> list) {
        return saveBatch(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateSelective(CarServiceRegion record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateVersionSelective(CarServiceRegion record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean exists(Wrapper<CarServiceRegion> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(CarServiceRegion entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<CarServiceRegion> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceRegion entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceRegion entity, Wrapper<CarServiceRegion> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<CarServiceRegion> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(CarServiceRegion entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(Wrapper<CarServiceRegion> queryWrapper) {
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
    public boolean updateById(CarServiceRegion entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(Wrapper<CarServiceRegion> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(CarServiceRegion entity, Wrapper<CarServiceRegion> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatchById(Collection<CarServiceRegion> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceRegion getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceRegion> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceRegion> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceRegion getOne(Wrapper<CarServiceRegion> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public long count() {
        return super.count();
    }

    @Override
    @Cacheable(sync = true)
    public long count(Wrapper<CarServiceRegion> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceRegion> list(Wrapper<CarServiceRegion> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceRegion> list() {
        return super.list();
    }

    @Override
    @Cacheable(sync = true)
    public List<Map<String, Object>> listMaps(Wrapper<CarServiceRegion> queryWrapper) {
        return super.listMaps(queryWrapper);
    }


}
