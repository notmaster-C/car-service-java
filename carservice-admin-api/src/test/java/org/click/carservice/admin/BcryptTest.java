package org.click.carservice.admin;

import org.click.carservice.core.utils.bcrypt.BCryptPasswordEncoder;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class BcryptTest {

    @Test
    public void test() {
        String rawPassword = "aaaaaa";
        String encodedPassword = "";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        encodedPassword = bCryptPasswordEncoder.encode(rawPassword);

        System.out.println("rawPassword=" + rawPassword + " encodedPassword=" + encodedPassword);

        Assert.assertTrue(bCryptPasswordEncoder.matches(rawPassword, encodedPassword));
    }
}
