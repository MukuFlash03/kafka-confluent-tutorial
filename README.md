## Apache Kafka on Confluent Cloud platform: 
#### Java, Spring Boot, Node.js, Python, Go
Tutorial [link](https://developer.confluent.io/get-started/java/)

.env.template file is provided. <br>
Please ensure .env file is present in the root directory.

### To start / stop all containers:

```
$ docker compose up -d
$ docker compose down
```

After running all containers, follow the instructions in the README.md file of the corresponding container's directory. <br>
Docker build and run commands can be skipped if using docker compose.

For instance, to run the Python container, refer to python/README.md.

```
$ python src/producer.py
$ python src/consumer.py
```