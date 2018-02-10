import random
import sys
import os
import glob
import time
import threading
import urllib.request
import requests

from urllib.parse import urlencode
from urllib.request import Request, urlopen
i = 0
a = [1, 2, 3, 4, 5, 6, 7]
b = [2, 4, 6, 8, 10, 12, 14]
c = [3, 6, 9, 12, 15, 18, 21]
d = [4, 8, 12, 16, 20, 24, 28]
e = [5, 10, 15, 20, 25, 30, 35]

def make_posts():
	global i
	global a,b,c,d,e

    

	url = 'http://13.55.201.70:8099/sensorReadings/' + str(a[i]) + '/' + str(b[i]) + '/' + str(c[i]) + '/' + str(d[i]) + '/' + str(e[i]) # Set destination URL here
    #post_fields = {'S1': 5}     # Set POST fields here
	r = requests.post(url, data = 0)



    #request = Request(url, urlencode(post_fields).encode())
    #json = urlopen(request).read().decode()
    #print(json)
	i = i + 1
	if(i == 7):
		i = 0
	time.sleep(1)

def get_data():
	url = 'http://13.55.201.70:8099/sensorReadings'
	r = requests.get(url, auth=('user', 'pass'))
	a = r.json
	print(a)
	time.sleep(2)
        
    
while True:
    make_posts()
    #get_data()

    



    
    
    
