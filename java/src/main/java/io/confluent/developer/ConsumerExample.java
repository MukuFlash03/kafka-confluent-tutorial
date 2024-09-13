package io.confluent.developer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.apache.kafka.common.config.SaslConfigs.SASL_JAAS_CONFIG;
import static org.apache.kafka.common.config.SaslConfigs.SASL_MECHANISM;

public class ConsumerExample {
    public static void main(final String[] args) {
        ReadEnvVars readEnv = new ReadEnvVars(".env");
        String confluentClusterApiKey = readEnv.get("CONFLUENT_CLUSTER_API_KEY");
        String confluentClusterApiSecret = readEnv.get("CONFLUENT_CLUSTER_API_SECRET");
        String bootstrapServerUrl = readEnv.get("BOOTSTRAP_SERVER_URL");

        final Properties props = new Properties() {{
            put(BOOTSTRAP_SERVERS_CONFIG, bootstrapServerUrl);
            put(SASL_JAAS_CONFIG, 
            String.format("org.apache.kafka.common.security.plain.PlainLoginModule required username='%s' password='%s';",
                confluentClusterApiKey, confluentClusterApiSecret)
            );
            put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
            put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getCanonicalName());
            put(GROUP_ID_CONFIG, "kafka-java-getting-started");
            put(AUTO_OFFSET_RESET_CONFIG, "earliest");
            put(SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            put(SASL_MECHANISM, "PLAIN");
        }};

        final String topic = "purchases";

        try (final Consumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Arrays.asList(topic));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.key();
                    String value = record.value();
                    System.out.println(String.format("Consumed event from topic %s: key = %-10s value = %s", topic, key, value));
                }
            }
        }
    }
}
