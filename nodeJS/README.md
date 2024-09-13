Tutorial for Apache Kafka and Node.js on Confluent Cloud platform: [Link](https://developer.confluent.io/get-started/nodejs)

Commands to build and run the project:

```
$ docker build -t ubuntu-nodejs . 
$ docker run -itd ubuntu-nodejs /bin/bash 

$ node src/producer.js
$ node src/consumer.js
```