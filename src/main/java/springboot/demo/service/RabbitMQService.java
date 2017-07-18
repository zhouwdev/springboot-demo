package springboot.demo.service;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 *
 * Created by AA on 2017/7/17.
 */
@Component
public class RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchange;

    @Value("${spring.rabbitmq.queue.routekey}")
    private String routeKey;

    public void sendMsg(String msg) {
        rabbitTemplate.convertAndSend(exchange, routeKey, msg);
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = "test_queue")
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception {
        try {
            // 解析RabbitMQ消息体
            String messageBody = new String(message.getBody());
            logger.info("from localhost rabbitmq message:" + messageBody);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
