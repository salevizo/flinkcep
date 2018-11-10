# Flink ICU

thelei java 8 gια na paixei to zookeeper kai kafka

evala java 8 etsi :https://stackoverflow.com/questions/44118443/downgrade-open-jdk-8-to-7-in-ubuntu-14-04\
m thn entolh p s leei 1,2,3 diallexe ti thes


https://github.com/ziyasal/scream-processing
mpainw sto fakelo tou kafka k trexw
#  Start Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties
# Start Kafka server
bin/kafka-server-start.sh config/server.properties

mpainw to flincep kai allazw to yml tou για ν τρχει το πραγειγμα αυτου στο youtube

tsekarw tis allages sto url
http://localhost:8081/#/overview


create topic
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic DEMOCP

list topics
bin/kafka-topics.sh --list --zookeeper localhost:2181 


afou ftiaxei to topic kalei ton kafka producer


flinkicu/producer
me to icu.py ftiaxnei 6000 events


/consumer
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --from-beginning --DEMOCP


kaneis mvn clean install gia na ftiaxei to .jar
xrhsimopoieis to jar-dependencies

//εδω τρεχεις το job για να δεις τα events στο http://localhost:8081/#/overview
sudo /home/cer/Downloads/flink-1.5.5/bin/flink run /home/cer/youtu/flinkicu/target/flinkicu-1.0-jar-with-dependencies.jar --topic DEMOCP --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --out file://home/cer/out.txt


pip install kafka-python

APO EDW TO VRHKA, KATEVASTE AN THELETE TON KWDIKA
from:https://www.youtube.com/watch?v=DkmQy8Nut84





