package org.kfk;

import org.apache.kafka.clients.consumer.*;
import java.time.Duration;
import java.util.*;

public class Consumer {
    public static void main(String[] args){

        String topics[] = {"strings"};

        // Set properties to connect Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("group.id","group01");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topics));

        try {
            while (true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records){
                    String message = String.format("offset=%d, key=%s, value=%s, partition=%s%n",
                            record.offset(),
                            record.key(),
                            record.value(),
                            record.partition());
                    System.out.printf(message);
                }

            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            consumer.close();
        }

    }
}
