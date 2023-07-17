package org.click.carservice.core;

import lombok.extern.slf4j.Slf4j;
import org.click.carservice.core.service.StatCoreService;
import org.click.carservice.core.utils.JacksonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class StatTest {


    @Autowired
    private StatCoreService coreService;

    @Test
    public void test() throws IOException {
        List<Map<String, Object>> user = coreService.statUser();
        List<Map<String, Object>> goods = coreService.statGoods();
        List<Map<String, Object>> order = coreService.statOrder();
        System.out.println(JacksonUtil.toJson(user));
        System.out.println("-------------------------");
        System.out.println(JacksonUtil.toJson(goods));
        System.out.println("-------------------------");
        System.out.println(JacksonUtil.toJson(order));
    }

}