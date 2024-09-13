package io.confluent.developer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Random;

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.apache.kafka.common.config.SaslConfigs.*;

public class ProducerExample {
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
            put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
            put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getCanonicalName());
            put(ACKS_CONFIG, "all");
            put(SECURITY_PROTOCOL_CONFIG, "SASL_SSL");
            put(SASL_MECHANISM, "PLAIN");
        }};

        final String topic = "purchases";

        String[] users = {"eabara", "jsmith", "sgarcia", "jbernard", "htanaka", "awalther"};
        String[] items = {"book", "alarm clock", "t-shirts", "gift card", "batteries"};

        try (final Producer<String, String> producer = new KafkaProducer<>(props)) {
            final Random rnd = new Random();
            final int numMessages = 10;
            for (int i = 0; i < numMessages; i++) {
                final String user = users[rnd.nextInt(users.length)];
                final String item = items[rnd.nextInt(items.length)];
                
                producer.send(new ProducerRecord<>(topic, user, item),
                (event, ex) -> {
                    if (ex != null)
                        ex.printStackTrace();
                    else
                        System.out.printf("Produced event to topic %s: key = %-10s value = %s\n", topic, user, item);
                });
            }
            System.out.printf("%s events were produced to topic %s \n", numMessages, topic);
        }
    }
}
