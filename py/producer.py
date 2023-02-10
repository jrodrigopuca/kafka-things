import time
from kafka import KafkaProducer
from datetime import datetime

producer = KafkaProducer(bootstrap_servers=['localhost:9092'])
topic = 'strings'

# send messages
for i in range(100):
    messageToSend = f'msg:{i} en {datetime.now()}'
    producer.send(topic,messageToSend.encode('utf-8'))
    print(messageToSend)
    time.sleep(2)