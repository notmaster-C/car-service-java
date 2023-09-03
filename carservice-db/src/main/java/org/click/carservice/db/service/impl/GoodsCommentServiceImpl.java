package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.click.carservice.db.domain.CarServiceGoodsComment;
import org.click.carservice.db.mapper.GoodsCommentMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.click.carservice.db.service.IGoodsCommentService;
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
@CacheConfig(cacheNames = "carservice_goods_comment")
public class GoodsCommentServiceImpl extends IBaseServiceImpl<GoodsCommentMapper, CarServiceGoodsComment> implements IGoodsCommentService {


    @Override
    //@Cacheable(sync = true)
    public CarServiceGoodsComment findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceGoodsComment> queryAll(Wrapper<CarServiceGoodsComment> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int add(CarServiceGoodsComment record) {
        return getBaseMapper().insert(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean batchAdd(List<CarServiceGoodsComment> list) {
        return saveBatch(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateSelective(CarServiceGoodsComment record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateVersionSelective(CarServiceGoodsComment record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean exists(Wrapper<CarServiceGoodsComment> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(CarServiceGoodsComment entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<CarServiceGoodsComment> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceGoodsComment entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceGoodsComment entity, Wrapper<CarServiceGoodsComment> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<CarServiceGoodsComment> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(CarServiceGoodsComment entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(Wrapper<CarServiceGoodsComment> queryWrapper) {
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
    public boolean updateById(CarServiceGoodsComment entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(Wrapper<CarServiceGoodsComment> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(CarServiceGoodsComment entity, Wrapper<CarServiceGoodsComment> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatchById(Collection<CarServiceGoodsComment> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    //@Cacheable(sync = true)
    public CarServiceGoodsComment getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceGoodsComment> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceGoodsComment> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    //@Cacheable(sync = true)
    public CarServiceGoodsComment getOne(Wrapper<CarServiceGoodsComment> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public long count() {
        return super.count();
    }

    @Override
    //@Cacheable(sync = true)
    public long count(Wrapper<CarServiceGoodsComment> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceGoodsComment> list(Wrapper<CarServiceGoodsComment> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    //@Cacheable(sync = true)
    public List<CarServiceGoodsComment> list() {
        return super.list();
    }

    @Override
    //@Cacheable(sync = true)
    public List<Map<String, Object>> listMaps(Wrapper<CarServiceGoodsComment> queryWrapper) {
        return super.listMaps(queryWrapper);
    }


}
