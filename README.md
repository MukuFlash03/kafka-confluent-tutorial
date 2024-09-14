## Apache Kafka on Confluent Cloud platform: 
#### Java, Spring Boot, Node.js, Python, Go ([Tutorial link](https://developer.confluent.io/get-started/java/))

<img width="1440" alt="Screenshot 2024-09-14 at 1 25 26 AM" src="https://github.com/user-attachments/assets/c1f39da1-9cf2-4edc-b3c6-9201f846a8fc">



-------

`.env.template` file is provided. <br>
Please ensure `.env` file is present in the root directory.

### To start / stop all containers:

```
$ docker compose up -d
$ docker compose down
```

----

After running all containers, follow the instructions in the README.md file of the corresponding container's directory. <br>
Docker build and run commands can be skipped if using docker compose.

For instance, to run the Python container, refer to python/README.md.

```
$ python src/producer.py
$ python src/consumer.py
```
