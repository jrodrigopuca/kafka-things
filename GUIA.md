# Kafka
## Instalar Kafka

sudo apt install openjdk-11-jdk
mkdir Downloads
curl https://downloads.apache.org/kafka/3.3.2/kafka_2.12-3.3.2.tgz -o Downloads/kafka.tgz
# Descomprimir
tar -xvzf ~/Downloads/kafka.tgz --strip 1

### modificar en kafka/config/server.properties
log.dirs= ~/kafka-logs

### modificar en kafka/config/zookeeper.properties
data.dir= ~/kafka-logs

ubicación en WSL
cd Users/[User]/AppData/Local/Packages/CanonicalGroupLimited.UbuntuonWindows_[CODE]/LocalState/rootfs

#---------------INICIO-------------------------------
Desde ~/Kafka:

## iniciar
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties

## para tener en cuenta
zookeeper localhost:2181
kafka-server (broker) localhost:9092

## En caso de error borrar logs (se pierden los topics)
rm -rf dirs/klogs dirs/zookeeper

#---------------TOPIC-------------------------------
## crear nuevo topic
bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --topic cities

### topic con particiones
bin/kafka-topics.sh \
--bootstrap-server localhost:9092 \
--create \
--replication-factor 1 \
--partitions 5 \
--topic numbers
## topic con particiones
bin/kafka-topics.sh \
--bootstrap-server localhost:9092,localhost:9093,localhost:9094  \
--create \
--replication-factor 1 \
--partitions 5 \
--topic cars

## topic con replicación
### Esto producirá 21 particiones en total = 7 partitions * 3 replications
### Para este caso creará 7 particiones en cada server
bin/kafka-topics.sh \
--bootstrap-server localhost:9092,localhost:9093,localhost:9094  \
--create \
--replication-factor 3 \
--partitions 7 \
--topic months

## Traer lista de topics
bin/kafka-topics.sh --list --bootstrap-server localhost:9092
### En múltiples brokers
bin/kafka-topics.sh --list --bootstrap-server localhost:9092,localhost:9093,localhost:9094

## Traer info de un topic
bin/kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic cities
### En múltiples brokers
bin/kafka-topics.sh --describe --bootstrap-server localhost:9092,localhost:9093,localhost:9094 --topic cars


#------------------CONSUMER OFFSETS------------------------------
#### Es un topic muy especial, solo aparece cuando algún mensaje fue consumido del clúster
## Traer info de los offsets o particiones de kafka
bin/kafka-topics.sh --describe --bootstrap-server localhost:9092 --topic __consumer_offsets
#### traer con m{ultiples

#-------------------PRODUCER--------------------------------
## Conectarse al producer
bin/kafka-console-producer.sh --broker-list localhost:9092 --topic cities

### conectar el producer a multiples brokers
bin/kafka-console-producer.sh --broker-list localhost:9092,localhost:9093,localhost:9094 --topic cars

#-------------------CONSUMER----------------------------------
## Conectarse al consumidor
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cities

### Consumir desde el principio
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cities --from-beginning
### Consumir desde una partición en específico
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic animals --from-beginning --partition 1
### Consumir desde un especifico offeset de una partición especifica
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic animals --offset 0 --partition 1
### Desde varios brokers
bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092,localhost:9093,localhost:9094 \
--topic cars
### Consumir con consumer group personalizado
bin/kafka-console-consumer.sh \
--bootstrap-server localhost:9092 \
--topic numbers \
--group numbers-group \
--from-beginning 


#-------------------CONSUMER GROUPS-----------------------------
## Listado de grupos
### Por defecto cada consumidor viene en consumer groups diferentes
### Lista de consumer groups (se incluyen también a los grupos de consumers inactivos)
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --list

### Info del consumer group
bin/kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group console-consumer-22028 --describe



#-----------------BROKERS----------------------------------

### Conocer los brokers activos
#### Muy importante para saber cuales siguen vivos y donde estan las replicas
bin/zookeeper-shell.sh localhost:2181 ls /brokers/ids

### Información sobre el broker conectados
bin/zookeeper-shell.sh localhost:2181 get /brokers/ids


#------------------MULTIPLES BROKERS--------------------------------------
## Tener más de un kafka server (brokers)
- Crear un nuevo archivo server.properties
- agregarle un nuevo id (no usado). broker.id=2
- descomentar la linea listener y asignarle un puerto, por default es 9092. listeners=PLAINTEXT://:9094
- agregarle la dirección donde iran los logs (logs.dirs)

bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server-00.properties
bin/kafka-server-start.sh config/server-01.properties
bin/kafka-server-start.sh config/server-02.properties

##----------------------
Suscribe: dinamicamente se asigna las particiones para seguir el topic
Assign: se asigna las particiones a observar