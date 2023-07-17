package org.click.carservice.db.mybatis;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageHelper;
import org.click.carservice.db.entity.PageBody;

import java.util.List;
import java.util.Objects;

/**
 * 自定义通用接口
 *
 * @param <T> 实体类
 * @author click
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 根据id查询数据
     *
     * @param id 主键
     * @return 实体类
     */
    T findById(String id);

    /**
     * 根据条件查询列表
     *
     * @param queryWrapper 条件
     * @return 实体类列表
     */
    List<T> queryAll(Wrapper<T> queryWrapper);

    /**
     * 根据条件查询是否存在
     *
     * @param queryWrapper 条件
     * @return true, false
     */
    boolean exists(Wrapper<T> queryWrapper);

    /**
     * 添加
     *
     * @param record 实体类
     * @return 影响行数
     */
    int add(T record);

    /**
     * 批量插入数据
     *
     * @param list 实体列表
     * @return 影响行数
     */
    boolean batchAdd(List<T> list);

    /**
     * 更新
     *
     * @param record 实体类
     * @return 影响行数
     */
    int updateSelective(T record);

    /**
     * 使用乐观锁更新
     *
     * @param record 实体类
     * @return 影响行数
     */
    int updateVersionSelective(T record);

    /**
     * 根据id删除
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);


    /**
     * 开启分页排序
     *
     * @param body 分页实体
     * @return 排序拼接字符串
     */
    default QueryWrapper<T> startPage(PageBody body) {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        if (body.getPage() != null && body.getLimit() != null && body.getLimit() > 0) {
            PageHelper.clearPage();
            PageHelper.startPage(body.getPage(), body.getLimit());
        }
        if (Objects.equals("asc", body.getOrder())) {
            wrapper.orderByAsc(body.getSort());
        } else {
            wrapper.orderByDesc(body.getSort());
        }
        if (body.getId() != null) {
            wrapper.eq("`id`", body.getId());
        }
        return wrapper;
    }

}
