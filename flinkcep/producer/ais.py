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




def write_csv(x):
    db=('doi105281zenodo1167595')
    if(x==0):
        query="SELECT lat, lon, status, turn, speed, heading, course, t , mmsi FROM public.nari_dynamic LIMIT 1000;"
    else:
        query="SELECT lat, lon, status, turn, speed, heading, course, t , mmsi FROM public.nari_dynamic OFFSET "+ str(x*1000)+" ROWS FETCH NEXT "+str(1000)+"ROWS ONLY;"
    con = psycopg2.connect(database = "doi105281zenodo1167595", user = "postgres", password = avgerosPass, host = "127.0.0.1", port = "5432")
    with con:
        ais_data= pd.read_sql_query(query, con)
    con.close()

    ais2=[]
    for i in range(len(ais_data['lon'])) :
        #print "1+"+str(ais_data["status"][i])
        #print i
        '''if ais_data['lat'][i] is None:
            print 1
            ais_data.loc['lat'][i]=0.0
        if ais_data['lon'][i] is None:
            print 2
            ais_data.loc['lon'][i]=0.0
        if ais_data['mmsi'][i] is None:
            print 3
            ais_data.loc['mmsi'][i]=0.0
        #print ais_data['status'][i]
        if ais_data['status'][i] is None:
            ais_data.loc[i]['status']=0
        if ais_data['speed'][i] is None:
            print 5
            ais_data.loc['speed'][i]=0.0
        if ais_data['turn'][i] is None:
            ais_data.loc[i]['turn']=0.0
        if ais_data['heading'][i] is None:
            print 7
            ais_data.loc['heading'][i]=0.0
        if ais_data['course'][i] is None:
            print 8
            ais_data.loc['course'][i]=0.0
        if ais_data['t'][i] is None:
            print 9
            ais_data.loc['t'][i]=0.0'''
        #print ais_data.loc[i]
        ais_data.fillna(value=0, inplace=True)
        #ais_data.fillna(0)
        #print ais_data.loc[i]
        #print "2+"+str(ais_data['status'][i])
        ais2.append({ "lat" : float(ais_data['lat'][i]), "lon" : float(ais_data['lon'][i]),"mmsi" :int(ais_data['mmsi'][i]), "status":int(ais_data['status'][i]), "speed":float(ais_data['speed'][i]),"turn":float(ais_data['turn'][i]),"heading":float(ais_data['heading'][i]), "course":float(ais_data['course'][i]), "t":int(ais_data['t'][i])})
    return ais2
    '''with open('mycsvfile.csv', 'wb') as f:  # Just use 'w' mode in 3.x
        for i in range(len(ais_data['lon'])) :
            w = csv.DictWriter(f, ['lat', 'lon', 'mmsi', 'status', 'speed', 'turn', 'heading', 'course','t'])
            w.writerow(ais2[i])'''



def main():

    ## the topic 
    topic = sys.argv[1]
    ## create a Kafka producer with json serializer
    producer = KafkaProducer(value_serializer=lambda v: json.dumps(v).encode('utf-8'),bootstrap_servers=server)
    print "*** Starting measurements stream on " + server + ", topic : " + topic
    csv_ctx=[]
    counter=0
    while(counter<10000):
        print "counter="+str(counter)
        csv_ctx=write_csv(counter)
        counter=counter+1
        #print "counter="+str(counter)
        #print len(csv_ctx)
        #print counter
        for i in range(len(csv_ctx)) :
            ais = { "lat" : float(csv_ctx[i]["lat"]), "lon" : float(csv_ctx[i]["lon"]),"mmsi" :int(csv_ctx[i]["mmsi"]), "status":int(csv_ctx[i]["status"]), "speed":float(csv_ctx[i]["speed"]),"turn":float(csv_ctx[i]["turn"]),"heading":float(csv_ctx[i]["heading"]), "course":float(csv_ctx[i]["course"]), "t":int(csv_ctx[i]["t"])}
            producer.send(topic, ais, key = b'%d'%1)
            #print "Sending AIS messages   : %s" % (json.dumps(ais).encode('utf-8'))
            #print i
                #sleep(2)



        #print "\nIntercepted user interruption ..\nBlock until all pending messages are sent.."
            producer.flush()
        print "Sleeping.."
        sleep(8)

if __name__ == "__main__":
    main()




