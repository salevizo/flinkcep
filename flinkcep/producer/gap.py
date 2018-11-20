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

avgerosPass="1992"
allOtherPass="2"
def main():

    ## the topic
    topic = sys.argv[1]
    ## create a Kafka producer with json serializer
    producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),bootstrap_servers=server)
    print "*** Starting measurements stream on " + server + ", topic : " + topic
    db=('doi105281zenodo1167595')


    query="SELECT lat, lon, status, turn, speed, heading, course,t FROM public.nari_dynamic LIMIT 1000;"
    con = psycopg2.connect(database = "doi105281zenodo1167595", user = "postgres", password = avgerosPass, host = "127.0.0.1", port = "5432")
    with con:
        ais_data= pd.read_sql_query(query, con)
    con.close()

    for i in range(9) :
        ## Generate random measurements
        if(i==0):
            ais = { "lat" : float(7.541122), "lon" : float(6.904849),"mmsi" : int(0), "status":int(ais_data['status'][i]), "speed":float(1992),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(1)}
        elif(i==1):
            ais = { "lat" : float(7.541122), "lon" : float(6.904849),"mmsi" : int(1), "status":int(ais_data['status'][i]), "speed":float(1993),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(29)}
        elif(i==2):
            ais = { "lat" : float(2.541122), "lon" : float(3.90484),"mmsi" : int(2), "status":int(ais_data['status'][i]), "speed":float(1993),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(39)}

        elif(i==3):
            ais = { "lat" : float(7.541122), "lon" : float(6.904849),"mmsi" : int(0), "status":int(ais_data['status'][i]), "speed":float(1993),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(45)}
        elif(i==4):
            ais = { "lat" : float(7.541122), "lon" : float(6.904849),"mmsi" : int(0), "status":int(ais_data['status'][i]), "speed":float(1992),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(49)}
        elif(i==5):
            ais = { "lat" : float(7.541122), "lon" : float(6.904849),"mmsi" : int(1), "status":int(ais_data['status'][i]), "speed":float(1993),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(74)}
        elif(i==6):
            ais = { "lat" : float(2.541122), "lon" : float(6.90484),"mmsi" : int(2), "status":int(ais_data['status'][i]), "speed":float(1993),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(78)}

        elif(i==7):
            ais = { "lat" : float(7.541122), "lon" : float(6.904849),"mmsi" : int(0), "status":int(ais_data['status'][i]), "speed":float(1993),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(87)}

        else:
            ais = { "lat" : float(7.541122), "lon" : float(6.90484),"mmsi" : int(1), "status":int(ais_data['status'][i]), "speed":float(122),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":float(100)}
        producer.send(topic, ais, key = b'%d'%i)
        print "[%d]Sending AIS messages   : %s" % (i,json.dumps(ais).encode('utf-8'))


    print "\nIntercepted user interruption ..\nBlock until all pending messages are sent.."
    producer.flush()

if __name__ == "__main__":
    main()




