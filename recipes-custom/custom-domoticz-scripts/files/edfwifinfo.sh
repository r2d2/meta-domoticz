#!/bin/sh

dev_papp=1126
dev_demain=1063

TELEINFO=$(curl -s 192.168.1.96/json)
PAPP=$(echo $TELEINFO | jq '.PAPP' )
DEMAIN=$(echo $TELEINFO | jq '.DEMAIN') 
BBRHCJB=$(echo $TELEINFO | jq '.BBRHCJB')
BBRHPJB=$(echo $TELEINFO | jq '.BBRHPJB')
BBRHCJW=$(echo $TELEINFO | jq '.BBRHCJW')
BBRHPJW=$(echo $TELEINFO | jq '.BBRHPJW')
BBRHCJR=$(echo $TELEINFO | jq '.BBRHCJR')
BBRHPJR=$(echo $TELEINFO | jq '.BBRHPJR')
ENERGY=$(($BBRHCJB+$BBRHPJB+$BBRHCJW+$BBRHPJW+$BBRHCJR+$BBRHPJR-184756488))

#echo "string:" $TELEINFO 

function send_data {
dev=$1
data1=$2
data2=$3

if [ ! "$data2" = "" ] 
then
echo "sending data $data1,$data2 to dev $dev"
curl -k "https://127.0.0.1:8443/json.htm?type=command&param=udevice&idx=$dev&svalue=$data1;$data2"

else if [ ! "$data1" = "" ]
then
echo "sending data $data1 to dev $dev"
curl -k "https://127.0.0.1:8443/json.htm?type=command&param=udevice&idx=$dev&svalue=$data1"
fi
fi

}

send_data $dev_papp "$PAPP" "$ENERGY" 
send_data $dev_demain "$DEMAIN"

