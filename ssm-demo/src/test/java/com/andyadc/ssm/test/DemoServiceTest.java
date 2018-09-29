package com.andyadc.ssm.test;

import com.andyadc.ssm.dto.DemoDTO;
import com.andyadc.ssm.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * @author andy.an
 * @since 2018/9/29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext.xml"})
public class DemoServiceTest {

    @Autowired
    private DemoService demoService;

    @Test
    public void testAdd() {
        DemoDTO dto = new DemoDTO();
        dto.setName("abc");
        dto.setCreatedTime(new Date());
        dto.setUpdatedTime(new Date());
        demoService.add(dto);
    }
}
