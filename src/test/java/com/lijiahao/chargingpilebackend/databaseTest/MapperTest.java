package com.lijiahao.chargingpilebackend.databaseTest;

import com.lijiahao.chargingpilebackend.entity.User;
import com.lijiahao.chargingpilebackend.mapper.ChargingPileStationMapper;
import com.lijiahao.chargingpilebackend.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MapperTest {

    Logger log = LoggerFactory.getLogger(MapperTest.class);

    private final UserMapper userMapper;

    @Autowired // 构造函数注入
    public MapperTest(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Test
    public void selcetTest(){
        List<User> userList = userMapper.selectList(null);
        log.info("查询结果： userList = {}", userList);
        assertEquals(userList.size(), 2);
    }

    @Test
    @Transactional // 开启事务
    public void insertTest() {
        User user = new User("13535853644 ", "lijiahao");
        int res = userMapper.insert(user);
        log.info("插入结果： res = {}  插入主键={}", res, user.getId());
        log.info("插入User, user = {}", user);
        assertEquals(res, 1);
        List<User> users = userMapper.selectList(null);
        log.info("查询结果： users = {}", users);
    }


    @Test
    @Transactional
    public void updateTest() {
        int res = userMapper.updateById(new User(1, "13535853647 ", "lijiahao", null));
        log.info("更新结果： res = {}", res);
        List<User> users = userMapper.selectList(null);
        log.info("查询结果： users = {}", users);
        assertEquals(res, 1);
    }

    @Test
    @Transactional
    public void deleteTest() {
        int res = userMapper.deleteById(1);
        log.info("删除结果： res = {}", res);
        List<User> users = userMapper.selectList(null);
        log.info("查询结果： users = {}", users);
        assertEquals(res, 1);
    }





}
