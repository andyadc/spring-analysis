package com.andyadc.bms.test;

import com.andyadc.bms.auth.repository.AuthUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;

@SpringBootTest
public class RepositoryTests {

    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private AuthUserRepository authUserRepository;

    @Test
    public void testJdbcTemplate() {
        System.out.println(jdbcTemplate);
    }

    @Test
    public void testAuthUser() {
        authUserRepository.findByUsername("twwx62u").ifPresent((u) -> {
            try {
                System.out.println(objectMapper.writeValueAsString(u));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }
}