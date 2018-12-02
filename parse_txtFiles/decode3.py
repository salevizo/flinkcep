
#!/usr/bin/python

import re
import csv
import sys



def main(argv):

    lines=[]
    path=sys.argv[1]
    fh = open(path)


 #0-vessel1:227705102,  1-vessel2:227574020 , 2-Gap_End_1:1457645565 ,  3-Gap_End_2:1457647184,  4-gbsg1x,  5-lon1:48.38216 ,  6-lat1:-4.4970617, 7-lon2:48.3797 , 8-Lat2 : -4.4974666 

    for line in fh:
        if  "--" not in line:
            line_=line.replace("Suspicious RendezVous : { Vessel_1 :", "")
            line_=line_.replace("Vessel_2 :", "")
            line_=line_.replace("Gap_End_1 :", "")
            line_=line_.replace("Gap_End_2 :", "")
            line_=line_.replace("GeoHash :", "")
            line_=line_.replace("Lon1 :", "")
            line_=line_.replace("Lat1 :", "")
            line_=line_.replace("Lon2 : ", "")
            line_=line_.replace("Lat2 : ", "")
            line_=line_.replace("}", "")
            l=line_.split(",")
           
            lines.append(l)
            
    fh.close()
 
    name=path.split('/')
    name_csv=name[-1].split('.')
    name_csv=name_csv[0] + '.csv'
    with open(name_csv, 'wb') as f:  # Just use 'w' mode in 3.x
            for i in range(len(lines)) :
                w = csv.writer(f, ['mmsi', 'lon', 'lat', 'geohash','gap_end'])
                text=[]
                text.append(lines[i][0]) #mmsi
                text.append(lines[i][5]) #lon
                text.append(lines[i][6]) #lat
                text.append(lines[i][4]) #geohash
                text.append(lines[i][2]) #gapend
                w.writerow(text)
                text=[]
                text.append(lines[i][1]) #mmsi
                text.append(lines[i][7]) #lon
                text.append(lines[i][8]) #lat
                text.append(lines[i][4]) #geohash
                text.append(lines[i][3]) #gapend
    print len(lines)*2
#x=lon, y=lat

if __name__ == "__main__":
   main(sys.argv[1:])
