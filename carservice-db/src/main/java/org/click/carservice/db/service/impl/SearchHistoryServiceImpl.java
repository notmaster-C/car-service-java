package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.click.carservice.db.domain.CarServiceSearchHistory;
import org.click.carservice.db.mapper.SearchHistoryMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.click.carservice.db.service.ISearchHistoryService;
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
 * 搜索历史表 服务实现类
 * </p>
 *
 * @author click
 */
@Service
@Primary
@CacheConfig(cacheNames = "carservice_search_history")
public class SearchHistoryServiceImpl extends IBaseServiceImpl<SearchHistoryMapper, CarServiceSearchHistory> implements ISearchHistoryService {


    @Override
    //@Cacheable(sync = true)
    public CarServiceSearchHistory findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceSearchHistory> queryAll(Wrapper<CarServiceSearchHistory> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int add(CarServiceSearchHistory record) {
        return getBaseMapper().insert(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean batchAdd(List<CarServiceSearchHistory> list) {
        return saveBatch(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateSelective(CarServiceSearchHistory record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateVersionSelective(CarServiceSearchHistory record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean exists(Wrapper<CarServiceSearchHistory> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(CarServiceSearchHistory entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<CarServiceSearchHistory> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceSearchHistory entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceSearchHistory entity, Wrapper<CarServiceSearchHistory> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<CarServiceSearchHistory> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(CarServiceSearchHistory entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(Wrapper<CarServiceSearchHistory> queryWrapper) {
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
    public boolean updateById(CarServiceSearchHistory entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(Wrapper<CarServiceSearchHistory> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(CarServiceSearchHistory entity, Wrapper<CarServiceSearchHistory> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatchById(Collection<CarServiceSearchHistory> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    //@Cacheable(sync = true)
    public CarServiceSearchHistory getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceSearchHistory> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceSearchHistory> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    //@Cacheable(sync = true)
    public CarServiceSearchHistory getOne(Wrapper<CarServiceSearchHistory> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public long count() {
        return super.count();
    }

    @Override
    //@Cacheable(sync = true)
    public long count(Wrapper<CarServiceSearchHistory> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceSearchHistory> list(Wrapper<CarServiceSearchHistory> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceSearchHistory> list() {
        return super.list();
    }

    @Override
    //@Cacheable(sync = true)
    public List<Map<String, Object>> listMaps(Wrapper<CarServiceSearchHistory> queryWrapper) {
        return super.listMaps(queryWrapper);
    }


}
