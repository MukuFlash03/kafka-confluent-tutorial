Tutorial for Apache Kafka and Spring Boot on Confluent Cloud platform: [Link](https://developer.confluent.io/get-started/spring-boot)

Commands to build and run the project:

```
$ docker build -t ubuntu-springboot . 
$ docker run -itd ubuntu-springboot /bin/bash 

$ gradle build
$ gradle bootRun --args='--producer'
$ gradle bootRun --args='--consumer'
```