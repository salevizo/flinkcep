Flink ICU
thelei java 8 gια na paixei to zookeeper kai kafka

evala java 8 etsi :https://stackoverflow.com/questions/44118443/downgrade-open-jdk-8-to-7-in-ubuntu-14-04\ m thn entolh p s leei 1,2,3 diallexe ti thes

https://github.com/ziyasal/scream-processing mpainw sto fakelo tou kafka k trexw

Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

Start Kafka server
bin/kafka-server-start.sh config/server.properties

mpainw to flincep kai allazw to yml tou για ν τρχει το πραγειγμα αυτου στο youtube

tsekarw tis allages sto url http://localhost:8081/#/overview

create topic bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic DEMOCP

list topics bin/kafka-topics.sh --list --zookeeper localhost:2181

afou ftiaxei to topic kalei ton kafka producer

flinkicu/producer me to icu.py ftiaxnei 6000 events

/consumer bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --DEMOCP

kaneis mvn clean install gia na ftiaxei to .jar xrhsimopoieis to jar-dependencies

//εδω τρεχεις το job για να δεις τα events στο http://localhost:8081/#/overview sudo /home/cer/Downloads/flink-1.5.5/bin/flink run /home/cer/youtu/flinkicu/target/flinkicu-1.0-jar-with-dependencies.jar --topic DEMOCP --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --out file://home/cer/out.txt

pip install kafka-python

APO EDW TO VRHKA, KATEVASTE AN THELETE TON KWDIKA from:https://www.youtube.com/watch?v=DkmQy8Nut84


--------------------------------------------------------------------------------------------------------------------------

# MY STEPS
1)Στο φάκελο flink_1.6.2:bin/start-cluster.sh

2)Στο φάκελο του kafka2.2:bin/zookeeper-server-start.sh config/zookeeper.properties

3)Στο φάκελο του kafka2.2:bin/kafka-server-start.sh config/server.properties

4)Στο φάκελο του kafka2.2:bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic DEMOCP
Εδώ φτιάχνουμε το topic με ονομα DEMOCP που θα το στείλει στο zookeeper που εχουμε ανοιξει από πάνω

5)Εκει που έχουμε το Project μας παμε και κάνουμε:

mvn clean install

Στο φάκελο που εχει το Pom.xml

Αφού τελειώσει αυτό,εκτελούμε το 

 sudo /home/cer/Downloads/flink-1.6.2/bin/flink run /home/cer/Desktop/cer_2/flinkce/flinkcep/target/flinkicu-1.0-jar-with-dependencies.jar --topic DEMOCP --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --out /home/cer/Desktop/out.txt
 
 To topic μπαινει σαν παράμετρος στο java Που έχουμε φτιάξει και παίρνει το ονομα του topic που εχουμε δημιουργήσει DEMOCP.To -out είναι το αρχείο που τρέχουμ.
 
 6) Στην σελίδα http://localhost:8081/#/overview εχουμε το νεο job μας,το java προγραμμα.
 
 7)Παμε πάλι στο Kafka2.2:bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --topic DEMOCP για να βλεπουμε τι μηνυματα πανε απο το Python προγραμμα που θα τρέξουμε στο consumer του kafka.
 
 8)Στο project μας στο φάκελο producer τρέχουμε το icu.py:./icu.py DEMOCP
 
 
* Delete topic:
Παμε πάλι στο Kafka2.2:bin/kafka-topics.sh --zookeeper localhost:2181 --delete --topic DEMOCP
 
 Κανονικα icu.py->Kafka Consumer->Flink->project μας και πρεπει το out.txt να εχει αρχίσει να έχει output.



################
An alalxeis kati sto prodcer/ais.py prepei an kanei delete to topic k meta plai create gai na parei thna llagh o consumer
# ΑΛΛΑΓΕΣ 15/11
/home/cer/Desktop/flink-1.6.2/bin/flink run /home/cer/Desktop/cer_2/cep_flinkcep/target/flinkicu-1.0-jar-with-dependencies.jar -topic DEMOCP2 --bootstrap.servers localhost:9092 --zookeeper.connect localhost:218
Ετσι τρέχεις το δευτερο job του flink που ειναι υπευθυνο για να πάρει τα gaps/speeds κλπ   και τα να τα επεξεργαστει για να βγάλει τα αποτελέσματα του complex event.


/home/cer/Desktop/flink-1.6.2/bin/flink run /home/cer/Desktop/cer_2/flinkcep/target/flinkicu-1.0-jar-with-dependencies.jar --topic DEMOCP --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --topic_output DEMOCP2

Ετσι ξεκινάει το βασικό job που έιχμαε και πριν μονο που εχει μια έξτρα παράμετρο που έβαλα την --topic_output DEMOCP2 που είναι το topic του kafka που γράφει το flink job το αποτέλεσμα
