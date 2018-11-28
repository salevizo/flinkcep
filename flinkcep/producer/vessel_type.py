
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
    query="SELECT type_name, sourcemmsi FROM   PUBLIC.nari_ais_static, PUBLIC.ship_types_list WHERE  ship_types_list.shiptype_min <= nari_ais_static.shiptype  AND ship_types_list.shiptype_max >=nari_ais_static.shiptype GROUP  BY type_name, sourcemmsi;"
    con = psycopg2.connect(database = "doi105281zenodo1167595", user = "postgres", password = 2, host = "127.0.0.1", port = "5432")
    with con:
        type_data= pd.read_sql_query(query, con)
  




    df = pd.DataFrame(type_data, columns=["type_name", "sourcemmsi"])
    types=df.groupby("type_name").sourcemmsi.apply(pd.Series.tolist)
    types.to_csv('vessel_type.csv')
    print types
    

def main():
    write_csv()
    

if __name__ == "__main__":
    main()




