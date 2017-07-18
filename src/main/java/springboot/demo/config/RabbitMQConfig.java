package springboot.demo.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 *
 * Created by AA on 2017/7/17.
 */

//@Configuration
//可以用自动配置
public class RabbitMQConfig {

    @Autowired
    private Environment env;

    @Bean //注册mq连接工厂
    public ConnectionFactory connectionFactory() throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(env.getProperty("mq.host").trim());
        connectionFactory.setPort(Integer.parseInt(env.getProperty("mq.port").trim()));
        connectionFactory.setVirtualHost(env.getProperty("mq.vhost").trim());
        connectionFactory.setUsername(env.getProperty("mq.username").trim());
        connectionFactory.setPassword(env.getProperty("mq.password").trim());
        return connectionFactory;
    }

    //缓存工厂
    @Bean
    public CachingConnectionFactory cachingConnectionFactory() throws Exception {
        return new CachingConnectionFactory(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() throws Exception {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory());
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin() throws Exception {
        return new RabbitAdmin(cachingConnectionFactory());
    }

    @Bean
    Queue queue() {
        String name = env.getProperty("mq.queue.name").trim();
        // 是否持久化
        boolean durable = StringUtils.isEmpty(env.getProperty("mq.queue.durable").trim())?true
                :Boolean.valueOf(env.getProperty("mq.queue.durable").trim());
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = StringUtils.isEmpty(env.getProperty("mq.queue.exclusive").trim())?false
                :Boolean.valueOf(env.getProperty("mq.queue.exclusive").trim());
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = StringUtils.isEmpty(env.getProperty("mq.queue.autoDelete").trim())?false
                :Boolean.valueOf(env.getProperty("mq.queue.autoDelete").trim());
        return new Queue(name, durable, exclusive, autoDelete);
    }

    @Bean
    TopicExchange exchange() {
        String name = env.getProperty("mq.exchange.name").trim();
        // 是否持久化
        boolean durable = StringUtils.isEmpty(env.getProperty("mq.exchange.durable").trim())?true
                :Boolean.valueOf(env.getProperty("mq.exchange.durable").trim());
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = StringUtils.isEmpty(env.getProperty("mq.exchange.autoDelete").trim())?false
                :Boolean.valueOf(env.getProperty("mq.exchange.autoDelete").trim());
        return new TopicExchange(name, durable, autoDelete);
    }

    @Bean
    Binding binding() {
        String routekey = env.getProperty("mq.queue.routekey").trim();
        return BindingBuilder.bind(queue()).to(exchange()).with(routekey);
    }

}
