Tutorial for Apache Kafka and Golang on Confluent Cloud platform: [Link](https://developer.confluent.io/get-started/go)

Commands to build and run the project:

```
$ docker build -t ubuntu-golang . 
$ docker run -itd ubuntu-golang /bin/bash 

$ go build -o out/producer src/producer.go
$ go build -o out/consumer src/consumer.go

$ ./out/producer
$ ./out/consumer
```