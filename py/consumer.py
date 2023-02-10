from kafka import KafkaConsumer

topic = 'strings'

consumer = KafkaConsumer(
  topic,
  bootstrap_servers=['localhost:9092'],
  group_id='group-05'
)

for message in consumer:
  #print(message)
  message_to_show = f'msg:{message.value.decode()} offset:{message.offset} en partition {message.partition}'
  print(message_to_show)