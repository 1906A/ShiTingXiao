package cn.jiyun;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AmqpApplicationTests {

    @Autowired
    AmqpTemplate amqpTemplate;
    @Test
    public void contextLoads() {
        String msg = "你好!rabbit";
        // 1:交换机 2:key 3: msg 消息体
        amqpTemplate.convertAndSend("item.test.exchange","*.*",msg);
    }

}
