package examples;

import examples.Producer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Arrays;

@SpringBootApplication
public class SpringBootWithKafkaApplication {
    private final Producer producer;
    
    @Value("${example.name}")
    String name;

    public static void main (String[] args) {
        SpringApplication application = new SpringApplication(SpringBootWithKafkaApplication.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        application.run(args);
    }

    @Bean
    public CommandLineRunner CommandRunnerBean() {
        return (args) -> {
            System.out.println("Name: " + name);
            for (String arg : args) {
                switch (arg) {
                    case "--producer":
                        runProducer();
                        break;
                    case "--consumer":
                        runConsumer();
                        break;
                    default:
                        System.out.println("Usage: java -jar spring-boot-with-kafka-0.0.1-SNAPSHOT.jar --producer|--consumer");
                }
            }
        };
    }

    @Autowired
    SpringBootWithKafkaApplication(Producer producer) {
        this.producer = producer;
    }

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private void runProducer() {
        Map<String, List<String>> messages = new HashMap<>();
        messages.put("awalther", Arrays.asList("t-shirts", "t-shirts"));
        messages.put("htanaka", Arrays.asList("t-shirts", "batteries", "t-shirts"));
        messages.put("eabara", Arrays.asList("t-shirts", "t-shirts"));
        messages.put("jsmith", Arrays.asList("book", "batteries", "gift card"));

        for (Map.Entry<String, List<String>> entry : messages.entrySet()) {
            String user = entry.getKey();
            List<String> items = entry.getValue();
            for (String item : items) {
                this.producer.sendMessage(user, item);
            }
        }
    }

    private void runConsumer() {
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("myConsumer");
        if (listenerContainer != null) {
            listenerContainer.start();
        } else {
            System.out.println("Consumer 'myConsumer' not found.");
        }
    }

}
