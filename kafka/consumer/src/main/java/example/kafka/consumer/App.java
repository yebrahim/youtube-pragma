package example.kafka.consumer;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

public class App {
    private static String kafkaHost = "localhost:9092";
    private static String topic = "fancy-topic";
    private static String groupName = "consumer-group1";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaHost);
        properties.put("key.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer",
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", groupName);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(topic));

        try {
            System.out.printf("Listening to topic %s...\n", topic);
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));
                for (ConsumerRecord<String, String> msg : records) {
                    System.out.printf("Consumed message: %s partition %s at %s\n", msg.value(),
                            msg.partition(), new SimpleDateFormat("ss:SSS").format(new Date()));
                    Thread.sleep(new Random().nextInt(100));
                }
            }
        } catch (Exception e) {
            System.out.println("Error consuming.." + e);
        } finally {
            consumer.close();
        }
    }
}
