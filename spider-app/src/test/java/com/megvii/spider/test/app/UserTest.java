package com.megvii.spider.test.app;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import com.alibaba.fastjson.JSONObject;
import com.megvii.dzh.spider.SpiderApplication;
import com.megvii.dzh.spider.domain.po.User;
import com.megvii.dzh.spider.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = {SpiderApplication.class})
@Slf4j
public class UserTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelectUser() {
        List<User> users = userMapper.selectAll();
        log.info("---> {}", JSONObject.toJSONString(users));
    }
}
