require('dotenv').config();

const Kafka = require('node-rdkafka');

function createConsumer(config, onData) {
    const consumer = new Kafka.KafkaConsumer(config, {
        'auto.offset.reset': 'earliest',
    });

    return new Promise((resolve, reject) => {
        consumer
            .on('ready', () => resolve(consumer))
            .on('data', onData);

        consumer.connect();
    });
}

async function consumerExample() {
    const config = {
        'bootstrap.servers': process.env.BOOTSTRAP_SERVER_URL,
        'sasl.username': process.env.CONFLUENT_CLUSTER_API_KEY,
        'sasl.password': process.env.CONFLUENT_CLUSTER_API_SECRET,

        'security.protocol': 'SASL_SSL',
        'sasl.mechanisms': 'PLAIN',
        'group.id': 'kafka-nodejs-getting-started',
    }

    let topic = "purchases"

    const consumer = await createConsumer(config, ({ key, value }) => {
        let k = key.toString().padEnd(10, ' ');
        console.log(`Consumed event from topic ${topic}: key = ${k} value = ${value}`);
    });

    consumer.subscribe([topic]);
    consumer.consume();

    process.on('SIGINT', () => {
        console.log('\nDisconnecting consumer ...');
        consumer.disconnect();
    });
}

consumerExample()
    .catch((err) => {
        console.error(`Something went wrong: ${err}`);
        process.exit(1);
    });