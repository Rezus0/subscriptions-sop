package com.example.sopconsumer;

import com.example.sopconsumer.websocket.MyWebSocketHandler;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SopConsumerApplication {

    private final String queueName = "goLiveQueue";
    private final MyWebSocketHandler webSocketHandler;

    public SopConsumerApplication(MyWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @Bean
    public Queue goLiveQueue() {
        return new Queue(queueName, false);
    }

    @RabbitListener(queues = queueName)
    public void listen(String message) {
        System.out.println(message);
        webSocketHandler.sendMessageToAll(message);
    }

    public static void main(String[] args) {
        SpringApplication.run(SopConsumerApplication.class, args);
    }

}
