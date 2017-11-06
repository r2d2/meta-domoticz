#!/bin/sh 

tempo=$(curl -s "http://domogeek.entropialux.com/tempoedf/now") 
curl -k "https://127.0.0.1:8443/json.htm?type=command&param=udevice&idx=5&svalue=$tempo"

