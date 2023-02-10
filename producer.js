const { Kafka } = require('kafkajs');

const kafkaClient = new Kafka({
  clientId: 'producer-01',
  brokers: ['localhost:9092']
});

const producer = kafkaClient.producer();
const topic = 'strings';
let number = 0;

const createMessage = async () => {
  const messageToSend = `msg:${number} en ${new Date()}`

  try {
    await producer.send({ topic, messages: [{ value: messageToSend }] });
    number++;
  } catch (error) {
    console.log(error);
  }
}

const run = async () => {
  await producer.connect();
  setInterval(createMessage, 1000);
}

run()
  .catch(console.error)