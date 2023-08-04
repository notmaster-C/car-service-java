package org.click.carservice.db.mybatis;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import org.click.carservice.db.entity.PageBody;

import java.util.Objects;

public interface IBasePageService<T> {

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
