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

def main():
    spamreader=0
    with open('/home/cer/Desktop/example.csv', 'rb') as csvfile:
        spamreader = csv.reader(csvfile, delimiter=',')
        next(spamreader)
    ## the topic 
        topic = sys.argv[1]
    ## create a Kafka producer with json serializer
        producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),bootstrap_servers=server)
        print "*** Starting measurements stream on " + server + ", topic : " + topic
        csv_ctx=[]
        counter=0
        for row in spamreader:
	    
            #print row[8]
            ais = { "lat" : float(row[0]), "lon" : float(row[1]),"mmsi" :int(row[8]), "status":int(row[2]), "speed":float(row[4]),"turn":float(row[3]),"heading":float(row[5]), "course":float(row[6]), "t":int(row[7])}
            producer.send(topic, ais, key = b'%d'%1)
            counter=counter+1
            producer.flush()
            if counter%50000==0:
                print 'Send '+str(counter)
            if counter==100000:
                return

if __name__ == "__main__":
    main()




