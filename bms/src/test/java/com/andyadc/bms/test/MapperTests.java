package com.andyadc.bms.test;

import com.andyadc.bms.auth.entity.AuthUser;
import com.andyadc.bms.auth.mapper.AuthMapper;
import com.andyadc.bms.auth.mapper.AuthUserMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MapperTests {

    @Resource
    public AuthUserMapper authUserMapper;
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private AuthMapper authMapper;

    @Test
    public void testAuthMapper() {
        authMapper.selectMenuByUserId(1L).forEach((m) -> {
            try {
                System.out.println(objectMapper.writeValueAsString(m));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testAuthUserMapper() throws Exception {
        AuthUser user = authUserMapper.findByUsername("twwx62u");
        System.out.println(objectMapper.writeValueAsString(user));
    }
}
