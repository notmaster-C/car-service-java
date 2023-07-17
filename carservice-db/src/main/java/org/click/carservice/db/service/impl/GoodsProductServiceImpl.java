package org.click.carservice.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.click.carservice.db.domain.CarServiceGoodsProduct;
import org.click.carservice.db.mapper.GoodsProductMapper;
import org.click.carservice.db.mybatis.IBaseServiceImpl;
import org.click.carservice.db.service.IGoodsProductService;
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
 * 商品货品表 服务实现类
 * </p>
 *
 * @author click
 */
@Service
@Primary
@CacheConfig(cacheNames = "carservice_goods_product")
public class GoodsProductServiceImpl extends IBaseServiceImpl<GoodsProductMapper, CarServiceGoodsProduct> implements IGoodsProductService {


    @Override
    @Cacheable(sync = true)
    public CarServiceGoodsProduct findById(String id) {
        return getBaseMapper().selectById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceGoodsProduct> queryAll(Wrapper<CarServiceGoodsProduct> queryWrapper) {
        return getBaseMapper().selectList(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int add(CarServiceGoodsProduct record) {
        return getBaseMapper().insert(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean batchAdd(List<CarServiceGoodsProduct> list) {
        return saveBatch(list);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateSelective(CarServiceGoodsProduct record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int updateVersionSelective(CarServiceGoodsProduct record) {
        return getBaseMapper().updateById(record);
    }

    @Override
    @CacheEvict(allEntries = true)
    public int deleteById(String id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean exists(Wrapper<CarServiceGoodsProduct> queryWrapper) {
        return getBaseMapper().exists(queryWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean save(CarServiceGoodsProduct entity) {
        return super.save(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveBatch(Collection<CarServiceGoodsProduct> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceGoodsProduct entity) {
        return super.saveOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdate(CarServiceGoodsProduct entity, Wrapper<CarServiceGoodsProduct> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean saveOrUpdateBatch(Collection<CarServiceGoodsProduct> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeById(CarServiceGoodsProduct entity) {
        return super.removeById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean remove(Wrapper<CarServiceGoodsProduct> queryWrapper) {
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
    public boolean updateById(CarServiceGoodsProduct entity) {
        return super.updateById(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(Wrapper<CarServiceGoodsProduct> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean update(CarServiceGoodsProduct entity, Wrapper<CarServiceGoodsProduct> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean updateBatchById(Collection<CarServiceGoodsProduct> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceGoodsProduct getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceGoodsProduct> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceGoodsProduct> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    @Cacheable(sync = true)
    public CarServiceGoodsProduct getOne(Wrapper<CarServiceGoodsProduct> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public long count() {
        return super.count();
    }

    @Override
    @Cacheable(sync = true)
    public long count(Wrapper<CarServiceGoodsProduct> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceGoodsProduct> list(Wrapper<CarServiceGoodsProduct> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    @Cacheable(sync = true)
    public List<CarServiceGoodsProduct> list() {
        return super.list();
    }

    @Override
    @Cacheable(sync = true)
    public List<Map<String, Object>> listMaps(Wrapper<CarServiceGoodsProduct> queryWrapper) {
        return super.listMaps(queryWrapper);
    }


}
