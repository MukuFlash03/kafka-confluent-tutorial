Tutorial for Apache Kafka and Java on Confluent Cloud platform: [Link](https://developer.confluent.io/get-started/java)

Commands to build and run the project:

```
$ gradle build
$ gradle shadowJar
$ java -cp build/libs/kafka-java-getting-started-0.0.1.jar io.confluent.developer.ProducerExample
$ java -cp build/libs/kafka-java-getting-started-0.0.1.jar io.confluent.developer.ConsumerExample
```