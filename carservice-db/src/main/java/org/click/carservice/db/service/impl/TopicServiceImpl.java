package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.click.carservice.db.domain.CarServiceTopic;
import org.click.carservice.db.mapper.TopicMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.click.carservice.db.service.ITopicService;
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
 * 专题表 服务实现类
 * </p>
 *
 * @author click
 */
@Service
@Primary
@CacheConfig(cacheNames = "carservice_topic")
public class TopicServiceImpl extends IBaseServiceImpl<TopicMapper, CarServiceTopic> implements ITopicService {


    @Override
    @Cacheable(sync = true)
    public CarServiceTopic findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceTopic> queryAll(Wrapper<CarServiceTopic> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int add(CarServiceTopic record) {
        return getBaseMapper().insert(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean batchAdd(List<CarServiceTopic> list) {
        return saveBatch(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateSelective(CarServiceTopic record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateVersionSelective(CarServiceTopic record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean exists(Wrapper<CarServiceTopic> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(CarServiceTopic entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<CarServiceTopic> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceTopic entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceTopic entity, Wrapper<CarServiceTopic> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<CarServiceTopic> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(CarServiceTopic entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(Wrapper<CarServiceTopic> queryWrapper) {
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
    public boolean updateById(CarServiceTopic entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(Wrapper<CarServiceTopic> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(CarServiceTopic entity, Wrapper<CarServiceTopic> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatchById(Collection<CarServiceTopic> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceTopic getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceTopic> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceTopic> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceTopic getOne(Wrapper<CarServiceTopic> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public long count() {
        return super.count();
    }

    @Override
    @Cacheable(sync = true)
    public long count(Wrapper<CarServiceTopic> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceTopic> list(Wrapper<CarServiceTopic> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceTopic> list() {
        return super.list();
    }

    @Override
    @Cacheable(sync = true)
    public List<Map<String, Object>> listMaps(Wrapper<CarServiceTopic> queryWrapper) {
        return super.listMaps(queryWrapper);
    }


}
