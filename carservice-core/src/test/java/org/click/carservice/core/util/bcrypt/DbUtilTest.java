package org.click.carservice.core.util.bcrypt;

import org.click.carservice.core.utils.DbUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class DbUtilTest {

    @Autowired
    private Environment environment;

    @Test
    public void testBackup() {
        File file = new File("test.sql");
        DbUtil.backup(file.getPath(), "carservice", "carservice123456", "carservice");
    }

    //    这个测试用例会重置carservice数据库，所以比较危险，请开发者注意
//    @Test
    public void testLoad() {
        File file = new File("test.sql");
        DbUtil.backup(file.getPath(), "carservice", "carservice123456", "carservice");
    }
}
