package com.sparrow.chat.email;

import com.sparrow.chat.boot.ApplicationBoot;
import com.sparrow.protocol.BusinessException;
import com.sparrow.spring.starter.test.SparrowTestExecutionListener;
import com.sparrow.utility.EMailUtility;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationBoot.class})
@TestExecutionListeners(listeners = {SparrowTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public class EmailTest {
    @Test
    public void test() throws BusinessException {
        EMailUtility eMailUtility = new EMailUtility();
        eMailUtility.sendMail("49200163@qq.com", "你好朋友在吗？", "在北京玩？", "zh_cn");
    }
}
