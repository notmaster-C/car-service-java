package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.click.carservice.db.domain.CarServiceNotice;
import org.click.carservice.db.mapper.NoticeMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.click.carservice.db.service.INoticeService;
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
 * 通知表 服务实现类
 * </p>
 *
 * @author click
 */
@Service
@Primary
@CacheConfig(cacheNames = "carservice_notice")
public class NoticeServiceImpl extends IBaseServiceImpl<NoticeMapper, CarServiceNotice> implements INoticeService {


    @Override
    //@Cacheable(sync = true)
    public CarServiceNotice findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceNotice> queryAll(Wrapper<CarServiceNotice> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int add(CarServiceNotice record) {
        return getBaseMapper().insert(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean batchAdd(List<CarServiceNotice> list) {
        return saveBatch(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateSelective(CarServiceNotice record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateVersionSelective(CarServiceNotice record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean exists(Wrapper<CarServiceNotice> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(CarServiceNotice entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<CarServiceNotice> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceNotice entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceNotice entity, Wrapper<CarServiceNotice> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<CarServiceNotice> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(CarServiceNotice entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(Wrapper<CarServiceNotice> queryWrapper) {
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
    public boolean updateById(CarServiceNotice entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(Wrapper<CarServiceNotice> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(CarServiceNotice entity, Wrapper<CarServiceNotice> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatchById(Collection<CarServiceNotice> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    //@Cacheable(sync = true)
    public CarServiceNotice getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceNotice> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceNotice> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    //@Cacheable(sync = true)
    public CarServiceNotice getOne(Wrapper<CarServiceNotice> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public long count() {
        return super.count();
    }

    @Override
    //@Cacheable(sync = true)
    public long count(Wrapper<CarServiceNotice> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceNotice> list(Wrapper<CarServiceNotice> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceNotice> list() {
        return super.list();
    }

    @Override
    //@Cacheable(sync = true)
    public List<Map<String, Object>> listMaps(Wrapper<CarServiceNotice> queryWrapper) {
        return super.listMaps(queryWrapper);
    }


}
