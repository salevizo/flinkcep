
#!/usr/bin/python

import re
import csv
import sys


#0-mmsi=228160000, 1-gapStart=1458153616{ 2- gapStartLot=-4.721238, 3-gapStartLat=48.222893} S, 4-gapEnd=1459292046{  5-gapEndLot=-4.32819, 6-gapEndLat=48.191254} , geoHash='gbsfjsb076dt'}
def main(argv):

    lines=[]
    path=sys.argv[1]
    fh = open(path)
    for line in fh:
        if  "--" not in line:
            line_=line.replace("SuspiciousFishing{mmsi=", "")
            line_=line_.replace("gapStart=", "")
            line_=line_.replace("{  gapStartLot=", ",")
            line_=line_.replace("gapStartLat=", "")           
            line_=line_.replace("gapEnd=", "")   
            line_=line_.replace("gapEndLot=", ",")     
            line_=line_.replace("gapEndLat=", "")    
            line_=line_.replace("geoHash=", "")    
            line_=line_.replace("{", "")        
            line_=line_.replace("}", "")    
            line_=line_.replace("S", "")      

            print line_  
            line_withouttags=line_.split(',')
            
            values=[]
            for i in line_withouttags:
                 txt= re.sub(r'[^0-9.-]', ' ', i)
                 values.append(txt)



            

            lines.append(values)
    fh.close()
    print len(lines[0])
    #print (lines)
    name=path.split('/')
    name_csv=name[-1].split('.')
    name_csv=name_csv[0] + '.csv'
    with open(name_csv, 'wb') as f:  # Just use 'w' mode in 3.x
            for i in range(len(lines)) :
                w = csv.writer(f, ['mmsi', 'lon', 'lat'])
                text=[]
                text.append(lines[i][0])
                text.append(lines[i][2])
                text.append(lines[i][3])
                print "----" + str(text)
                w.writerow(text)
                text=[]
                text.append(lines[i][0])
                text.append(lines[i][5])
                text.append(lines[i][6])
                w.writerow(text)
#x=lon, y=lat

if __name__ == "__main__":
   main(sys.argv[1:])
