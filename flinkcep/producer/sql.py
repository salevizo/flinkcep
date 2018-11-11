
import pandas as pd
from kafka import KafkaProducer
from random import gauss
from time import sleep
import sys
import json
import psycopg2


server = "localhost:9092"


db=('doi105281zenodo1167595')
query="SELECT * FROM public.nari_ais_static LIMIT 15"


con = psycopg2.connect(database = "doi105281zenodo1167595", user = "postgres", password = "1992", host = "127.0.0.1", port = "5432")
## the topic
topic = sys.argv[1]

## create a Kafka producer with json serializer
producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),
                         bootstrap_servers=server)

print "*** Starting measurements stream on " + server + ", topic : " + topic

with con:
    ais_data= pd.read_sql_query(query, con)
    print ais_data
con.close()

try:
    counter=0
    while True:
        for record in ais_data :
            ## Generate random measurements
            meas1 = record
            producer.send(topic, meas1, key = b'%d'%counter)
            counter=counter+1
            #meas2 = { "userid":"%d"%userId, "type" : "TEMP", "value" : getTemperature()}
            #producer.send(topic, meas2, key = b'%d'%userId)

            #meas3 = { "userid":"%d"%userId, "type" : "SBP", "value" : getSystolicBloodPressure()}
            #producer.send(topic, meas3, key = b'%d'%userId)

            #print "Sending HR   : %s" % (json.dumps(meas1).encode('utf-8'))
            #print "Sending TEMP : %s" % (json.dumps(meas2).encode('utf-8'))
            print "Sending BP   : %s" % (json.dumps(meas1).encode('utf-8'))

        sleep(1)

except KeyboardInterrupt:
    pass