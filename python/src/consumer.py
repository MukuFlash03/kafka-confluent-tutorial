#!/usr/bin/env python

from confluent_kafka import Consumer
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
        'group.id': 'kafka-python-getting-started',
        'auto.offset.reset': 'earliest'
    }
        
    consumer = Consumer(config)

    topic = 'purchases'
    consumer.subscribe([topic])

    try:
        while True:
            msg = consumer.poll(1.0)
            if msg is None:
                # Initial message consumption may take up to
                # `session.timeout.ms` for the consumer group to
                # rebalance and start consuming
                print("Waiting...")
            elif msg.error():
                print("ERROR: {}".format(msg.error()))
            else:
                print("Consumed event from topic {topic}: key = {key:12} value = {value:12}".format(
                    topic=msg.topic(), key=msg.key().decode('utf-8'), value=msg.value().decode('utf-8')
                ))
    except KeyboardInterrupt:
        pass
    finally:
        consumer.close()
