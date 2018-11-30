--------------------------------------------------------------------------------------------------------------------------

# MY STEPS
1)Στο φάκελο flink_1.6.2:bin/start-cluster.sh

2)Στο φάκελο του kafka2.2:bin/zookeeper-server-start.sh config/zookeeper.properties

3)Στο φάκελο του kafka2.2:bin/kafka-server-start.sh config/server.properties

4)Στο φάκελο του kafka2.2:bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic DEMOCP
Στο φάκελο του kafka2.2:bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic DEMOCP2
Στο φάκελο του kafka2.2:bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic  DEMOCP_CO
Στο φάκελο του kafka2.2:bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic  DEMO_ACC


5)Εκει που έχουμε τa Project μας παμε και κάνουμε:

mvn clean install

6) 2 Projects
1st: python->kafka_producer->kafka_consumer->flinkcep :recognize tranjectory events
2nd: results for 1st project->kafka producer->kafka consumer->cep_flinkcep:recognize complex events


7)run the jars

RUN IT AS SUDO USER!!!!!
TO SEE LOGS: GO AT flink_1.6.2/log and check flink-cer-taskexecutor-0-cer.out, you will see all systemOut logs
1st--run the project for topic DEMOCP2
sudo /home/cer/Desktop/flink-1.6.2/bin/flink run  /home/cer/flinkcep/flinkcep/cep_flinkcep/target/flinkicu_cep-1.0-jar-with-dependencies.jar --topic_gap DEMOCP2 --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --topic_co DEMOCP_CO

2d--run th project for DEMOCP whose results will be ised as input for topic DEMOCP2
sudo /home/cer/Desktop/flink-1.6.2/bin/flink run /home/cer/flinkcep/flinkcep/flinkcep/target/flinkicu-1.0-jar-with-dependencies.jar --topic DEMOCP --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --out /home/cer/Desktop/out.txt --topic_output_acc DEMO_ACC --topic_output_gap DEMOCP2 --topic_output_co DEMOCP_CO


 
 8) Στην σελίδα http://localhost:8081/#/overview εχουμε τa job μας.
 
 9)Παμε πάλι στο Kafka2.2:bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic DEMOCP για να βλεπουμε τι μηνυματα πανε απο το Python προγραμμα που θα τρέξουμε στο consumer του kafka.
 
10)Παμε πάλι στο Kafka2.2:bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic DEMOCP2 to check messages received on cep_flinkcep
 

 8)Στο project μας στο φάκελο producer τρέχουμε το ./ais.py DEMOCP --topic_output DEMOCP2
or ./gap.py DEMOCP  (hardcoded data to test our implemented scenarios)
που είναι το topic του kafka που γράφει το flink job το αποτέλεσμα
here the ais messages start to produce

9)to delete the flink jobs
flink-1.6.2$ bin/flink list
and delete jobId
flink-1.6.2$ bin/flink cancel 833958573e917dd87a42a7dec1f7310f 
 
* Delete topic:
Παμε πάλι στο Kafka2.2:bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic DEMOCP
 
An alalxeis kati sto prodcer/ais.py prepei an kanei delete to topic k meta plai create gai na parei thn allagh o consumer

### NEW WAY TO RUN
sudo /home/cer/Desktop/flink-1.6.2/bin/flink run /home/cer/Desktop/cer_2/flinkcep/target/flinkicu-1.0-jar-with-dependencies.jar --INPUT AIS --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --OUT_GAP GAP --OUT_COTRAVEL COTRAVEL --OUT_COURSE COURSE

sudo /home/cer/Desktop/flink-1.6.2/bin/flink run /home/cer/Desktop/cer_2/cep_flinkcep/target/flinkicu_cep-1.0-jar-with-dependencies.jar --IN_GAP GAP --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --IN_COTRAVEL COTRAVEL --IN_COURSE COURSE 



############visualize################################
Vazeis ta apotelesmata twn events se ena csv, oi diafores times xwrizontai me koma
pas qgis Destop, sundeesai sth vash,kaneis drag and drop ena shapefile apo auta pou einai diathesima aristera
epeita add new layer, add delimited text layer kai vazeis to txt sou ,diallegeis poia tis lat kai poia lon sthles k oti to arxeio einai seperated me koma,
epeita patas next kai ws coordinate systme selector epilegeis: WGS 84
etsi  de xreiazetai na ta valeis sth vash
