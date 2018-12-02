
#!/usr/bin/python

import re
import csv
import sys



def main(argv):

    lines=[]
    path=sys.argv[1]
    fh = open(path)
    for line in fh:
        if  "--" not in line:
            line_=line.replace("Package Picking Vessels:Vessel_1:", "")
            line_=line_.replace("Longitude_1:", "")
            line_=line_.replace("Latitude_1:", "")
            line_=line_.replace("Vessel_2:", "")
            line_=line_.replace("Longitude_2:", " ")
            line_=line_.replace("Latitude_2:", "")
            line_=line_.replace("Time:", " ")
            line_=line_.replace("\n", "")
            l=line_.split(" ")
           
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
                text.append(lines[i][1])
                text.append(lines[i][2])
                text.append(lines[i][6])
                w.writerow(text)
                text=[]
                text.append(lines[i][3])
                text.append(lines[i][4])
                text.append(lines[i][5])
                text.append(lines[i][6])
                w.writerow(text)
    print len(lines)*2
#x=lon, y=lat

if __name__ == "__main__":
   main(sys.argv[1:])
