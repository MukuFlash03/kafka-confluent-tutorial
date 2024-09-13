require('dotenv').config({ path: '.env' });

const Kafka = require('node-rdkafka');

function createProducer(config, onDeliveryReport) {
    const producer = new Kafka.Producer(config);

    return new Promise((resolve, reject) => {
        producer
            .on('ready', () => {
                console.log('Producer is ready');
                resolve(producer);
            })
            .on('delivery-report', onDeliveryReport)
            .on('event.error', (err) => {
                console.error('event.error', err);
                reject(err);
            })
            .on('event.log', function (log) {
                console.log('event.log', log);
            });

        console.log('Connecting producer...');
        producer.connect();
    });
}

async function producerExample() {
    const config = {
        'bootstrap.servers': process.env.BOOTSTRAP_SERVER_URL,
        'sasl.username': process.env.CONFLUENT_CLUSTER_API_KEY,
        'sasl.password': process.env.CONFLUENT_CLUSTER_API_SECRET,
        'security.protocol': 'SASL_SSL',
        'sasl.mechanisms': 'PLAIN',
        'acks': 'all',
        'dr_msg_cb': true
    }

    let topic = "purchases"

    let users = ["eabara", "jsmith", "sgarcia", "jbernard", "htanaka", "awalther"];
    let items = ["book", "alarm clock", "t-shirts", "gift card", "batteries"];

    try {
        const producer = await createProducer(config, (err, report) => {
            if (err) {
                console.warn("Error producing: ", err);
            } else {
                const { topic, key, value } = report;
                let k = key.toString().padEnd(10, ' ');
                console.log(`Produced event to topic ${topic}: key = ${k} value = ${value}`);
            }
        });

        console.log('Producer created successfully');

        let numEvents = 10;
        for (let idx = 0; idx < numEvents; ++idx) {
            const key = users[Math.floor(Math.random() * users.length)];
            const value = Buffer.from(items[Math.floor(Math.random() * items.length)]);
            try {
                producer.produce(topic, -1, value, key);
                console.log(`Produced event ${idx + 1}`);
            } catch (err) {
                console.error(`Error producing event ${idx + 1}:`, err);
            }
        }

        console.log('All events produced, flushing...');
        producer.flush(10000, () => {
            console.log('Producer flushed');
            producer.disconnect();
        });
    } catch (err) {
        console.error(`Error in producerExample: ${err}`);
    }
}

producerExample()
    .catch((err) => {
        console.error(`Something went wrong: \n${err}`);
        process.exit(1);
    });

console.log('Script finished');