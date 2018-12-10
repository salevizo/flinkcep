#!/usr/bin/python

import re
import csv
import sys

#SuspiciousSpeedNearPort{mmsi_1=228051000, lon=-4.4801335, lat=48.379547, timestamp=1453678018}

def main(argv):

    lines=[]
    path=sys.argv[1]
    fh = open(path)
    for line in fh:
        if  "--" not in line:
            line_=line.replace("SuspiciousSpeedNearPort{mmsi_1=", "")
            line_=line.replace("lon=", "")
            line_=line.replace("lat=", "")
            line_=line.replace("timestamp=", "")
            line_=line_.replace("}", "")
            line_=line_.replace("\n", "")
        
            line_withouttags=line_.split(',')
            values=[]
            for i in line_withouttags:
                 txt= re.sub(r'[^0-9.-]', ' ', i)
                 values.append(txt)


            

            lines.append(values)
    fh.close()
    print len(lines)
    mmsis=[]
    name=path.split('/')
    name_csv=name[-1].split('.')
    name_csv=name_csv[0] + '.csv'
    with open(name_csv, 'wb') as f:  # Just use 'w' mode in 3.x
            for i in range(len(lines)) :
                w = csv.writer(f, ['mmsi',  'lon', 'lat', 'timestamp'])
                w.writerow(lines[i])
                mmsis.append(lines[i][0])

    mmsis=set(mmsis)
    print "mmsis are:" + str(mmsis)
    print len(mmsis)

#x=lon, y=lat

if __name__ == "__main__":
   main(sys.argv[1:])
