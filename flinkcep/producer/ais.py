#!/usr/bin/python

from kafka import KafkaProducer
from random import gauss
from time import sleep
import sys
import json

import sqlite3 as lite
import numpy as np
import pandas as pd
import psycopg2



server = "localhost:9092"


def main():

    ## the topic 
    topic = sys.argv[1]
    ## create a Kafka producer with json serializer
    producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),bootstrap_servers=server)
    print "*** Starting measurements stream on " + server + ", topic : " + topic
    db=('doi105281zenodo1167595')






    query="SELECT lat, lon, status, turn, speed, heading, course, t FROM public.nari_dynamic LIMIT 10;"
    con = psycopg2.connect(database = "doi105281zenodo1167595", user = "postgres", password = "2", host = "127.0.0.1", port = "5432")
    with con:
        ais_data= pd.read_sql_query(query, con)
    con.close()

    try:
        while True:
            for i in range(len(ais_data['lon'])) :
                ## Generate random measurements
                ais = { "lat" : float(ais_data['lat'][i]), "lon" : float(ais_data['lon'][i]),"mmsi" : "227741610", "status":int(ais_data['status'][i]), "speed":float(ais_data['speed'][i]),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(ais_data['t'][i])}

                producer.send(topic, ais, key = b'%d'%i)	
                print "Sending AIS messages   : %s" % (json.dumps(ais).encode('utf-8'))
    except KeyboardInterrupt:
        pass

	    
    print "\nIntercepted user interruption ..\nBlock until all pending messages are sent.."
    producer.flush()

if __name__ == "__main__":
    main()



