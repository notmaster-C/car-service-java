package org.click.carservice.wx;

import org.click.carservice.db.domain.CarServiceOrder;
import org.click.carservice.db.service.IOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class WxConfigTest {
    @Autowired
    private Environment environment;
    @Autowired
    private IOrderService orderService;

    @Test
    public void test() {
        CarServiceOrder order = orderService.findById("438");
        order.setOrderSn("1213123123123");
        System.out.println(order);
        System.out.println("影响行数：" + orderService.updateVersionSelective(order));

        CarServiceOrder order1 = orderService.findById("438");
        order.setOrderSn("1213123123123");
        System.out.println(order1);
        System.out.println("影响行数：" + orderService.updateVersionSelective(order1));
//        System.out.println("影响行数："+orderService.updateWithOptimisticLocker(order));
//        System.out.println("影响行数："+orderService.updateWithOptimisticLocker(order));


//        // 测试获取application-core.yml配置信息
//        System.out.println(environment.getProperty("carservice.express.appId"));
//        // 测试获取application-db.yml配置信息
//        System.out.println(environment.getProperty("spring.datasource.druid.url"));
//        // 测试获取application-wx.yml配置信息
//        System.out.println(environment.getProperty("carservice.wx.app-id"));
//        // 测试获取application-wx.yml配置信息
//        System.out.println(environment.getProperty("carservice.wx.notify-url"));
//        // 测试获取application.yml配置信息
//        System.out.println(environment.getProperty("logging.level.org.click.carservice.wx"));
    }

}
