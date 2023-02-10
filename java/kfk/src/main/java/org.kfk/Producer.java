package org.kfk;

import java.util.*;
import org.apache.kafka.clients.producer.*;

public class Producer {
    public static void main(String[] args){
        String clientId = "my-producer";
        String topicName = "strings";
        int maxRecords = 100;

        // Set properties to connect Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("compression.type","gzip");
        props.put("client.id", clientId);

        // Create Producer
        KafkaProducer<String,String> producer = new KafkaProducer<String, String>(props);

        // Send msg
        try{
            for(int i=0; i<maxRecords; i++){
                String message = String.format("Producer %s - message %s at %s", clientId, i, new Date());

                System.out.println(message);
                producer.send(new ProducerRecord<>(topicName, Integer.toString(i), message ));
                Thread.sleep(300);
            }
        }catch (Exception e){
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}
