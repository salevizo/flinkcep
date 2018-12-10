
#!/usr/bin/python

import re
import csv
import sys



def main(argv):

    lines=[]
    path=sys.argv[1]
    fh = open(path)


#SuspiciousCoTravellingVessels{mmsi_1=227730220, mmsi_2=235095836, lon1=-4.474815, lat1=48.340786, lon2=-4.4754534, lat2=48.341175, timestamp=1454114132}
    for line in fh:
        if  "--" not in line:
            line_=line.replace("SuspiciousCoTravellingVessels{mmsi_1=", "")
            line_=line_.replace("mmsi_2=", "")
            line_=line_.replace("lon1=", "")
            line_=line_.replace("lat1=", "")
            line_=line_.replace("lon2=", "")
            line_=line_.replace("lat2=", "")
            line_=line_.replace("}", "")
            l=line_.split(",")
           
            lines.append(l)
            
    fh.close()
 
    name=path.split('/')
    name_csv=name[-1].split('.')
    name_csv=name_csv[0] + '.csv'
    mmsis=[]
    with open(name_csv, 'wb') as f:  # Just use 'w' mode in 3.x
            for i in range(len(lines)) :
                w = csv.writer(f, ['mmsi', 'lon', 'lat'])
                text=[]
                text.append(lines[i][0]) #mmsi
                text.append(lines[i][2]) #lon
                text.append(lines[i][3]) #lat
                mmsis.append(lines[i][0])
                w.writerow(text)
                text=[]
                text.append(lines[i][1]) #mmsi
                text.append(lines[i][4]) #lon
                text.append(lines[i][5]) #lat
                mmsis.append(lines[i][1])
                w.writerow(text)
    print len(lines)*2
    mmsis=set(mmsis)
    print "mmsis are:" + str(mmsis)
    print len(mmsis)
#x=lon, y=lat

if __name__ == "__main__":
   main(sys.argv[1:])
