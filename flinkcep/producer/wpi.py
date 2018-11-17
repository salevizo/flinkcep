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
    query="SELECT latitude, longitude FROM  ports.wpi_ports;"
    con = psycopg2.connect(database = "doi105281zenodo1167595", user = "postgres", password = 2, host = "127.0.0.1", port = "5432")
    with con:
        ais_data= pd.read_sql_query(query, con)
    con.close()

   
    ais2=[]
    for i in range(len(ais_data['latitude'])-1) :
        if np.isnan(ais_data['latitude'][i]):
            ais_data['latitude'][i]=0.0
        if np.isnan(ais_data['longitude'][i]):
            ais_data['longitude'][i]=0.0
        ais2.append({ "latitude" : float(ais_data['latitude'][i]), "longitude" : float(ais_data['longitude'][i])});

    with open('wpi.csv', 'wb') as f:  # Just use 'w' mode in 3.x
        for i in range(len(ais_data['latitude'])-1) :
          

            w = csv.DictWriter(f, ['latitude', 'longitude'])
            w.writerow(ais2[i])
         

def main():
    write_csv()
    

if __name__ == "__main__":
    main()




