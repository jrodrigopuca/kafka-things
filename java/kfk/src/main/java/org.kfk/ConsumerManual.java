package org.kfk;

import org.apache.kafka.clients.consumer.*;
import java.io.*;
import java.time.Duration;
import java.util.*;

public class ConsumerManual {
    public static void main(String[] args) throws IOException {

        String topics[] = {"strings"};
        int minSize = 50;

        // Set properties to connect Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers","localhost:9092");
        props.put("group.id","group02");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");


        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topics));
        List<ConsumerRecord<String,String>> buffer = new ArrayList<ConsumerRecord<String,String>>();
        FileWriter fileWriter = new FileWriter("./numbers.txt", true);

        try {
            while (true){
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                // receive messages
                for (ConsumerRecord<String, String> record : records){
                    buffer.add(record);
                    String message = String.format("offset=%d, key=%s, value=%s, partition=%s%n",
                            record.offset(),
                            record.key(),
                            record.value(),
                            record.partition());
                    System.out.printf(message);
                }
                System.out.println("Buffer size: "+ buffer.size());

                // save on file to past messages
                if(buffer.size()>=minSize){
                    fileWriter.append(buffer.toString());
                    consumer.commitSync();
                    buffer.clear();
                }

            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            consumer.close();
            fileWriter.close();
        }

    }
}
