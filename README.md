The logic of the project:
--------------------------------------------------------------------------------------------------------------------------
1st: python->kafka_producer->kafka_consumer->flinkcep :recognize tranjectory events
2nd: results for 1st project->kafka producer->kafka consumer->cep_flinkcep:recognize complex events
--------------------------------------------------------------------------------------------------------------------------

Running commands:

1)link_1.6.2:bin/start-cluster.sh

2)kafka2.2:bin/zookeeper-server-start.sh config/zookeeper.properties

3)kafka2.2:bin/kafka-server-start.sh config/server.properties

4)kafka2.2:bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic AIS
kafka2.2:bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic OUT_GAP
kafka2.2:bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic  OUT_COTRAVEL
kafka2.2:bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic  OUT_COURSE

7)run the jars  -RUN IT AS SUDO USER!!!!!-
To see logs: GO AT flink_1.6.2/log and check flink-cer-taskexecutor-0-cer.out, you will see all systemOut logs

#run the project cep_flinkcep:
sudo /home/cer/Desktop/flink-1.6.2/bin/flink run /home/cer/Desktop/cer/flinkcep/cep_flinkcep/target/flinkicu_cep-1.0-jar-with-dependencies.jar --IN_GAP GAP --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --IN_COTRAVEL COTRAVEL --IN_COURSE COURSE

#run the project flinkcep: 
sudo /home/cer/Desktop/flink-1.6.2/bin/flink run /home/cer/Desktop/cer/flinkcep/flinkcep/target/flinkicu-1.0-jar-with-dependencies.jar calhost:9092 --zookeeper.connect localhost:2181 --OUT_GAP GAP --OUT_COTRAVEL COTRAVEL --OUT_COURSE COURSE

8) Check the running jobs onhttp://localhost:8081/#/overview εχουμε τa job μας.
 
9)Check consumer context for a kafka topic Kafka2.2:bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic AIS 
 
10)Inside the path /flinkcep/producer go at producer and run the script for ais messages ./ais.py AIS (fulfill the kafka producer with ais messages)

11)Outcomes of the project will be saved at the path /home/cer /Desktop/temp


Notes:
1)to delete the flink jobs
    fink-1.6.2$ bin/flink list
2) delete jobId
       flink-1.6.2$ bin/flink cancel 833958573e917dd87a42a7dec1f7310f 
3) Delete kafka topic:
        Kafka2.2:bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic AIS
 
4)At the path /convert_txt_to_csv there are some python scripts used to convert .txt files (outcome of jobs in flinkcep) into csv format


