Tutorial for Apache Kafka and Python on Confluent Cloud platform: [Link](https://developer.confluent.io/get-started/python)

Commands to build and run the project:

```
$ docker build -t ubuntu-python . 
$ docker run -itd ubuntu-python /bin/bash 

$ python src/producer.py
$ python src/consumer.py
```