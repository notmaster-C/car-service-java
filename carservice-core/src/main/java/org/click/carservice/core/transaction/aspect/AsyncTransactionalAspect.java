package org.click.carservice.core.transaction.aspect;

/**
 * Copyright (c) [click] [927069313@qq.com]
 * [carservice-plus] is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.baomidou.dynamic.datasource.tx.ConnectionFactory;
import com.baomidou.dynamic.datasource.tx.ConnectionProxy;
import com.baomidou.dynamic.datasource.tx.LocalTxUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.click.carservice.core.transaction.annotation.AsyncTransactional;
import org.click.carservice.core.transaction.context.AsyncTransactionalContextHolder;
import org.click.carservice.core.utils.Inheritable.InheritableRunnable;
import org.springframework.stereotype.Component;

/**
 * @author click
 */
@Slf4j
@Aspect
@Component
public class AsyncTransactionalAspect {

    @Around("@annotation(transactional)")
    public Object transactionalAspect(ProceedingJoinPoint joinPoint, AsyncTransactional transactional) throws Throwable {
        String ds = DynamicDataSourceContextHolder.peek();
        ConnectionProxy connection = ConnectionFactory.getConnection(ds);
        InheritableRunnable inheritableRunnable = new InheritableRunnable() {
            @Override
            public void runTask() {
                LocalTxUtil.startTransaction();
                ConnectionFactory.putConnection(ds, connection);
                try {
                    // 执行方法
                    joinPoint.proceed();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        };
        if (transactional.value()) {
            AsyncTransactionalContextHolder.setThreadFinishTasks(inheritableRunnable);
        } else {
            AsyncTransactionalContextHolder.setThreadTasks(inheritableRunnable);
        }
        return null;
    }

}
