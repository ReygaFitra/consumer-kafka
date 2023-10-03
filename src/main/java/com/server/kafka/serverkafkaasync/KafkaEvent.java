package com.server.kafka.serverkafkaasync;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;


@Component
@Slf4j
public class KafkaEvent {
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    // KafkaListener echoes the correlation ID and determines the reply topic
    @KafkaListener(groupId="${myproject.consumer-group}", topics = "${myproject.send-topics}", concurrency = "10")
    @SendTo
    public Message<?> listen(ConsumerRecord<String, Object> consumerRecord) throws InterruptedException, ExecutionException {

        log.info("incoming data from client : {}", consumerRecord.value().toString());
        String reversedString = new StringBuilder( String.valueOf(consumerRecord.value()) ).reverse().toString();

        Thread.sleep(10000);

        return MessageBuilder.withPayload( reversedString )
                .build();
    }

}
