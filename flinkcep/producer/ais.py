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
import csv


server = "localhost:9092"
avgerosPass="1992"
allOtherPass="2"




def write_csv():
    db=('doi105281zenodo1167595')
    query="SELECT lat, lon, status, turn, speed, heading, course, t , mmsi FROM public.nari_dynamic ORDER BY t ASC LIMIT 1000;"
    con = psycopg2.connect(database = "doi105281zenodo1167595", user = "postgres", password = 2, host = "127.0.0.1", port = "5432")
    with con:
        ais_data= pd.read_sql_query(query, con)
    con.close()

    ais2=[]
    for i in range(len(ais_data['lon'])) :
        if np.isnan(ais_data['lat'][i]):
            ais_data['lat'][i]=0.0
        if np.isnan(ais_data['lon'][i]):
            ais_data['lon'][i]=0.0
        if np.isnan(ais_data['mmsi'][i]):
            ais_data['mmsi'][i]=0
        if np.isnan(ais_data['status'][i]):
            ais_data['status'][i]=0
        if np.isnan(ais_data['speed'][i]):
            ais_data['speed'][i]=0.0
        if np.isnan(ais_data['turn'][i]):
            ais_data['turn'][i]=0.0
        if np.isnan(ais_data['heading'][i]):
            ais_data['heading'][i]=0.0
        if np.isnan(ais_data['course'][i]):
            ais_data['course'][i]=0.0
        if np.isnan(ais_data['t'][i]):
            ais_data['t'][i]=0
        ais2.append({ "lat" : float(ais_data['lat'][i]), "lon" : float(ais_data['lon'][i]),"mmsi" :int(ais_data['mmsi'][i]), "status":int(ais_data['status'][i]), "speed":float(ais_data['speed'][i]),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":int(ais_data['t'][i])})

    with open('mycsvfile.csv', 'wb') as f:  # Just use 'w' mode in 3.x
        for i in range(len(ais_data['lon'])) :
            w = csv.DictWriter(f, ['lat', 'lon', 'mmsi', 'status', 'speed', 'turn', 'heading', 'course','t'])
            w.writerow(ais2[i])

def main():
    #write_csv()
    ## the topic 
    topic = sys.argv[1]
    ## create a Kafka producer with json serializer
    producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),bootstrap_servers=server)
    print "*** Starting measurements stream on " + server + ", topic : " + topic
    csv_ctx=[]
    with open('mycsvfile.csv', 'rb') as csvfile:
        reader = csv.reader(csvfile, delimiter=' ', quotechar='|')
        for row in reader:
            csv_ctx.append((row[0].split(',')))


    try:
        while True:
            for i in range(len(csv_ctx)) :
                ais = { "lat" : float(csv_ctx[i][0]), "lon" : float(csv_ctx[i][1]),"mmsi" :int(csv_ctx[i][2]), "status":int(csv_ctx[i][3]), "speed":float(csv_ctx[i][4]),"turn":float(csv_ctx[i][5]),"heading":float(csv_ctx[i][6]), "course":float(csv_ctx[i][7]), "t":int(csv_ctx[i][8])}
                producer.send(topic, ais, key = b'%d'%i)	
                print "Sending AIS messages   : %s" % (json.dumps(ais).encode('utf-8'))
    except KeyboardInterrupt:
        pass

        print "\nIntercepted user interruption ..\nBlock until all pending messages are sent.."
        producer.flush()

if __name__ == "__main__":
    main()




