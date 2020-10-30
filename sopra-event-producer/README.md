El proposito de este poc es poder crear un proyecto mínimo y viable para interactuar con un Event Store, en este caso 
Kafka.

Voy a usar la plataforma Confluent para tener en un mismo sitio Zookeeper y Kafka. Por comodidad, voy a usar Docker.
# Prerequisitos 
1. Instalar Docker 
2. git

# clonar este repo
    git clone https://github.com/confluentinc/cp-all-in-one
    cd cp-all-in-one
    git checkout 6.0.0-post
    cd cp-all-in-one-community
    docker-compose up -d

    docker-compose ps

bash-3.2$ docker-compose ps
     Name                    Command                  State                                        Ports                                  
------------------------------------------------------------------------------------------------------------------------------------------
broker            /etc/confluent/docker/run        Up             0.0.0.0:29092->29092/tcp, 0.0.0.0:9092->9092/tcp, 0.0.0.0:9101->9101/tcp
connect           /etc/confluent/docker/run        Up (healthy)   0.0.0.0:8083->8083/tcp, 9092/tcp                                        
ksql-datagen      bash -c echo Waiting for K ...   Up                                                                                     
ksqldb-cli        /bin/sh                          Up                                                                                     
ksqldb-server     /etc/confluent/docker/run        Up             0.0.0.0:8088->8088/tcp                                                  
rest-proxy        /etc/confluent/docker/run        Up             0.0.0.0:8082->8082/tcp                                                  
schema-registry   /etc/confluent/docker/run        Up             0.0.0.0:8081->8081/tcp                                                  
zookeeper         /etc/confluent/docker/run        Up             0.0.0.0:2181->2181/tcp, 2888/tcp, 3888/tcp          

# El test EmbeddedKafkaLatchTest necesita un topic llamado test-topic-1

docker-compose exec broker kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 \
 --partitions 1 --topic test-topic-1
    
# Creamos un topic llamado users
    
    docker-compose exec broker kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic users

# Creamos un topic llamado pageviews
    
    docker-compose exec broker kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic pageviews

# Optional
# Install a Kafka Connector and Generate Sample Data. Pushing data to pageviews topic.
    
    curl -L -O -H 'Accept: application/vnd.github.v3.raw' https://api.github.com/repos/confluentinc/kafka-connect-datagen/contents/config/connector_pageviews_cos.config
    curl -X POST -H 'Content-Type: application/json' --data @connector_pageviews_cos.config http://localhost:8083/connectors

# Pushing data to users topic.
    
    curl -L -O -H 'Accept: application/vnd.github.v3.raw' https://api.github.com/repos/confluentinc/kafka-connect-datagen/contents/config/connector_users_cos.config

    curl -X POST -H 'Content-Type: application/json' --data @connector_users_cos.config http://localhost:8083/connectors

# Create and Write to a Stream and Table using ksqlDB. Se levanta una instancia y una terminal de ksqldb-cli
    
    docker-compose exec ksqldb-cli ksql http://ksqldb-server:8088

# Create a stream pageviews from the Kafka topic pageviews, specifying the value_format of AVRO
    
    CREATE STREAM pageviews (viewtime BIGINT, userid VARCHAR, pageid VARCHAR) WITH (KAFKA_TOPIC='pageviews', VALUE_FORMAT='AVRO');

# Create a table users with several columns from the Kafka topic users, with the value_format of AVRO:
    
    CREATE TABLE users (registertime BIGINT, gender VARCHAR, regionid VARCHAR, userid VARCHAR PRIMARY KEY) WITH (KAFKA_TOPIC='users', VALUE_FORMAT='AVRO');

# Writing Queries to ksqldb-cli
# Set the auto.offset.reset` query property to ``earliest. This instructs ksqlDB queries to read all available topic data from the beginning.

    SET 'auto.offset.reset'='earliest';

# Create a non-persistent query that returns data from a stream with the results limited to a maximum of three rows

    SELECT pageid FROM pageviews EMIT CHANGES LIMIT 3;

# Create a persistent query (as a stream) that filters pageviews stream for female users. The results from this query are written to the Kafka PAGEVIEWS_FEMALE topic
# Create a persistent query where the regionid ends with 8 or 9. Results from this query are written to a Kafka topic named pageviews_enriched_r8_r9:
# Create a persistent query that counts the pageviews for each region and gender combination in a tumbling window of 30 seconds when the count is greater than 1. 
# Because the procedure is grouping and counting, the result is now a table, rather than a stream. 
# Results from this query are written to a Kafka topic called PAGEVIEWS_REGIONS:

    CREATE TABLE pageviews_regions AS SELECT gender, regionid , COUNT(*) AS numusers FROM pageviews LEFT JOIN users ON pageviews.userid = users.userid WINDOW TUMBLING (size 30 second) GROUP BY gender, regionid HAVING COUNT(*) > 1;

# 
    SHOW STREAMS;
#

    SHOW TABLES;

#

    DESCRIBE EXTENDED users;
#
    
    SHOW QUERIES;

#
    
    EXPLAIN CSAS_PAGEVIEWS_FEMALE_0

# Monitor Streaming Data. Some queries...
    
    SELECT * FROM pageviews_female EMIT CHANGES;
    SELECT * FROM pageviews_female_like_89 EMIT CHANGES;
    SELECT * FROM pageviews_regions EMIT CHANGES;
# Stop Docker

    docker container ls -a -q

# paramos contenedores de la plataforma confluent.

    docker container stop $(docker container ls -a -q -f "label=io.confluent.docker")

# Ojito con este comando.
# After stopping the Docker containers, run the following commands to prune the Docker system. Running these commands deletes containers, networks, volumes, and images, freeing up disk space:
# docker system prune -a -f --volumes

# Administracion.

Tener en cuenta que lanzar comandos contra la plataforma es ir contra docker, por lo que todos los comandos deben ser
 del tipo:

    docker-compose exec broker kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic users
 
Siendo esto el comando típico que lanzarias contra un kafka que tuvieras instalado en tu máquina local.

    kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic users
    
# Troubleshooting
# Puede que al levantar todos los servicios, te encuentres algun puerto ocupado. Para ello hay que averiguar cual es el contenedor y pararlo.
# Pongamos que al levantar los contenedores te dice que el puerto 8088 está ocupado. 
lsof -i -P|grep -i "8088"
# Averiguas el contenedor
docker ps | grep 8088
# El primer valor es el identificador del contenedor.
# ejemplo:
# bash-3.2$ docker ps | grep 8088
# a4e346db2532   confluentinc/cp-ksqldb-server:6.0.0 "/etc/confluent/dock…"   About an hour ago   Up 3 minutes 0.0.0.0:8088->8088/tcp ksqldb-server
# paramos el proceso 
# docker stop a4e346db2532

# Puede que necesites crear una app spring-boot que tenga que reprocesar mensajes de nuevo.

# https://cloud.spring.io/spring-cloud-stream-binder-kafka/spring-cloud-stream-binder-kafka.html#kafka-dlq-processing

# enlaces

https://docs.confluent.io/current/quickstart/cos-docker-quickstart.html
