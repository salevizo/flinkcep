
#!/usr/bin/python

import re
import csv
import sys

#SuspiciousCoTravellingVessels{mmsi_1=228344800, mmsi_2=235087041, lon1=-4.5361185, lat1=48.349285, lon2=-4.5371885, lat2=-4.5371885, timestamp=1453416293}

def main(argv):

    lines=[]
    path=sys.argv[1]
    fh = open(path)
    for line in fh:
        if  "--" not in line:
            line_=line.replace("SuspiciousCoTravellingVessels{mmsi_1=", "")
            line_=line_.replace("mmsi_2=", "")
            line_=line_.replace("lon1=", "")
            line_=line_.replace("lat1=", "")
            line_=line_.replace("lon2=", " ")
            line_=line_.replace("lat2=", "")
            line_=line_.replace("timestamp=", " ")
            line_=line_.replace("\n", "")
            line_=line_.replace("}", "")
            l=line_.split(",")
           
            lines.append(l)
            
    fh.close()
 
    name=path.split('/')
    name_csv=name[-1].split('.')
    name_csv=name_csv[0] + '.csv'
    with open(name_csv, 'wb') as f:  # Just use 'w' mode in 3.x
            for i in range(len(lines)) :
                w = csv.writer(f, ['mmsi', 'lon', 'lat', 't'])
                text=[]
                text.append(lines[i][0])
                text.append(lines[i][2])
                text.append(lines[i][3])
                text.append(lines[i][6])
                w.writerow(text)
                text=[]
                text.append(lines[i][1])
                text.append(lines[i][4])
                text.append(lines[i][5])
                text.append(lines[i][6])
                w.writerow(text)
    print len(lines)*2
#x=lon, y=lat

if __name__ == "__main__":
   main(sys.argv[1:])