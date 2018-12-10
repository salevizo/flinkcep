
#!/usr/bin/python

import re
import csv
import sys

['', '', ' ', '228037700', '48.16899', '-4.4613566']


def main(argv):

    lines=[]
    path=sys.argv[1]
    fh = open(path)
    for line in fh:
        if  "--" not in line:
            line_=line.replace("Suspicious Loitering : {", "")
            line_=line_.replace("}", "")
            line_=line_.replace("\n", "")

        
            line_withouttags=line_.split(' ')
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
                w = csv.writer(f, ['mmsi', 'lat', 'lon'])
                w.writerow(lines[i])
                mmsis.append(lines[i][3])

    mmsis=set(mmsis)
    print "mmsis are:" + str(mmsis)
    print len(mmsis)
#x=lon, y=lat

if __name__ == "__main__":
   main(sys.argv[1:])
