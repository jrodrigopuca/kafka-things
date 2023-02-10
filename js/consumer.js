const { Kafka } = require('kafkajs');

const kafkaClient = new Kafka({
  clientId: 'consumer-01',
  brokers: ['localhost:9092']
});

const consumer = kafkaClient.consumer({ groupId: 'group-04' })
const topic = 'strings';

const receiveMessage = async ({ partition, message }) => {
  const messageToShow = `msg:${message.value.toString()} offset:${message.offset} en partition ${partition}`
  console.log(messageToShow)
}


const run = async () => {
  await consumer.connect()

  await consumer.subscribe({ topic })

  await consumer.run({
    eachMessage: receiveMessage
  })

}

run()
  .catch(console.error)