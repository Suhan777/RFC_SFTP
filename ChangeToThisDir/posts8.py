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

    

	url = 'http://13.55.201.70:8099/sensorReadings/' + str(a[i]) + '/' + str(b[i]) + '/' + str(c[i]) 