package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.click.carservice.db.domain.CarServiceComment;
import org.click.carservice.db.mapper.CommentMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.click.carservice.db.service.ICommentService;
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
 * 评论表 服务实现类
 * </p>
 *
 * @author click
 */
@Service
@Primary
@CacheConfig(cacheNames = "carservice_comment")
public class CommentServiceImpl extends IBaseServiceImpl<CommentMapper, CarServiceComment> implements ICommentService {


    @Override
    @Cacheable(sync = true)
    public CarServiceComment findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceComment> queryAll(Wrapper<CarServiceComment> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int add(CarServiceComment record) {
        return getBaseMapper().insert(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean batchAdd(List<CarServiceComment> list) {
        return saveBatch(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateSelective(CarServiceComment record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateVersionSelective(CarServiceComment record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean exists(Wrapper<CarServiceComment> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(CarServiceComment entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<CarServiceComment> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceComment entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceComment entity, Wrapper<CarServiceComment> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<CarServiceComment> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(CarServiceComment entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(Wrapper<CarServiceComment> queryWrapper) {
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
    public boolean updateById(CarServiceComment entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(Wrapper<CarServiceComment> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(CarServiceComment entity, Wrapper<CarServiceComment> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatchById(Collection<CarServiceComment> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceComment getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceComment> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceComment> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceComment getOne(Wrapper<CarServiceComment> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public long count() {
        return super.count();
    }

    @Override
    @Cacheable(sync = true)
    public long count(Wrapper<CarServiceComment> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceComment> list(Wrapper<CarServiceComment> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceComment> list() {
        return super.list();
    }

    @Override
    @Cacheable(sync = true)
    public List<Map<String, Object>> listMaps(Wrapper<CarServiceComment> queryWrapper) {
        return super.listMaps(queryWrapper);
    }


}
