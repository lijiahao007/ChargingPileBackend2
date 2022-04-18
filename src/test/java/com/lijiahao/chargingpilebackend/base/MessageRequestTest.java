package com.lijiahao.chargingpilebackend.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lijiahao.chargingpilebackend.controller.requestparam.MessageRequest;
import com.lijiahao.chargingpilebackend.entity.Message;
import com.lijiahao.chargingpilebackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MessageRequestTest {
    public static void main(String[] args) throws JsonProcessingException {
        List<TestUser> list = MessageRequestTest.getList();
        list.add(new TestUser("name3", 3, 30));
        list.forEach(System.out::println);
    }

    static List<TestUser> getList() {
        ArrayList<TestUser> list =  new ArrayList<TestUser>() {{
            add(new TestUser("name1", 1, 10));
            add(new TestUser("name2", 2, 20));
        }};
        return list;
    }
    


}

@AllArgsConstructor
@Data
@NoArgsConstructor
class TestUser{
    String name;
    int id;
    int age;
}
