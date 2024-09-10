package examples;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

public class SimpleConsumer {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Enter topic name");
            return;
        }
        //Kafka consumer configuration settings
        String topicName = args[0];

        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        //Kafka Consumer subscribes list of topics here.
        consumer.subscribe(List.of(topicName));

    //print the topic name
        System.out.println("Subscribed to topic " + topicName);

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
            for (ConsumerRecord<String, String> record : records)
// print the offset,key and value for the consumer records.
                System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());

        }
    }
}

/**
 * build: ./gradlew build
 *        ./gradlew shadowJar
 *
 * run: java -cp "build/libs/producer_consumer-0.0.1.jar:/home/atulya/lib/kafka_2.12-3.8.0/libs/*" examples.SimpleConsumer Hello-Kafka
 *
 * listen with: /bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic Hello-Kafka --from-beginning
 */