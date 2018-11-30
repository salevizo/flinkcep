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
    query="SELECT  st_x(st_centroid(geom)) as lon ,st_y(st_centroid(geom)) as lat, geom FROM  ports.ports_of_brittany;"
    con = psycopg2.connect(database = "doi105281zenodo1167595", user = "postgres", password = 1992, host = "127.0.0.1", port = "5432")
    with con:
        ais_data= pd.read_sql_query(query, con)
    con.close()

   
    ais2=[]
    for i in range(len(ais_data['lat'])) :
        if np.isnan(ais_data['lat'][i]):
            ais_data['lat'][i]=0.0
        if np.isnan(ais_data['lon'][i]):
            ais_data['lon'][i]=0.0
        ais2.append({ "lat" : float(ais_data['lat'][i]), "lon" : float(ais_data['lon'][i])});

    with open('wpi.csv', 'wb') as f:  # Just use 'w' mode in 3.x
        for i in range(len(ais_data['lat'])) :
          

            w = csv.DictWriter(f, ['lat', 'lon'])
            w.writerow(ais2[i])
         

def main():
    write_csv()
    

if __name__ == "__main__":
    main()




