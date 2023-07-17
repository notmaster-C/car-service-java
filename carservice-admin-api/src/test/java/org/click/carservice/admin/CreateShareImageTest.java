package org.click.carservice.admin;

import org.click.carservice.core.service.QrcodeCoreService;
import org.click.carservice.db.domain.carserviceGoods;
import org.click.carservice.db.service.IGoodsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateShareImageTest {
    @Autowired
    QrcodeCoreService qCodeService;
    @Autowired
    IGoodsService goodsService;

    @Test
    public void test() {
        carserviceGoods goods = goodsService.findById("1181010");
        qCodeService.createGoodShareImage(goods);
    }
}
