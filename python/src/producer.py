#!/usr/bin/env python

from random import choice
from confluent_kafka import Producer
from dotenv import load_dotenv
import os

load_dotenv()

if __name__ == '__main__':
    config = {
        'bootstrap.servers': os.getenv('BOOTSTRAP_SERVER_URL'),
        'sasl.username': os.getenv('CONFLUENT_CLUSTER_API_KEY'),
        'sasl.password': os.getenv('CONFLUENT_CLUSTER_API_SECRET'),

        'security.protocol': 'SASL_SSL',
        'sasl.mechanisms': 'PLAIN',
        'acks': 'all'
    }

    producer = Producer(config)

    # Optional per-message delivery callback (triggered by poll() or flush())
    # when a message has been successfully delivered or permanently
    # failed delivery (after retries).
    def delivery_callback(err, msg):
        if err:
            print('ERROR: Message failed delivery: {}'.format(err))
        else:
            print("Produced event to topic {topic}: key = {key:12} value = {value:12}".format(
                topic=msg.topic(), key=msg.key().decode('utf-8'), value=msg.value().decode('utf-8')
            ))
    
    topic = 'purchases'
    users = ['eabara', 'jsmith', 'sgarcia', 'jbernard', 'htanaka', 'awalther']
    products = ['book', 'alarm clock', 't-shirts', 'gift card', 'batteries']

    count = 0
    for _ in range(10):
        user = choice(users)
        product = choice(products)
        producer.produce(topic, user, product, callback=delivery_callback)
        count += 1
    
    # Block until the messages are sent.
    producer.poll(10000)
    producer.flush()