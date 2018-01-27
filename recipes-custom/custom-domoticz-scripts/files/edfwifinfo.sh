#!/bin/sh

# Parse options
while getopts ":t" opt; do
  case $opt in
    t)
      echo "test mode enabled (no domoticz publication)" >&2
      TEST=1
      ;;
    \?)
      echo "Invalid option: -$OPTARG" >&2
      ;;
  esac
done

# Check that jq is available
JQ=$(which jq)

if [ -z $JQ ] ; then
  echo "jq not installed on the system, please install it."
  exit 1
fi

# some hardcoded value, installation dependent.
dev_papp=1126
dev_tarif=5
dev_tarif_demain=1063

WIFINFOSRV="192.168.1.96"
DOMOTICZSRV="127.0.0.1:8443"
INITIALVALUE=184756488

TELEINFO=$(curl -s $WIFINFOSRV/json)

PAPP=$(echo $TELEINFO | jq '.PAPP' )
DEMAIN=$(echo $TELEINFO | jq '.DEMAIN')
BBRHCJB=$(echo $TELEINFO | jq '.BBRHCJB')
BBRHPJB=$(echo $TELEINFO | jq '.BBRHPJB')
BBRHCJW=$(echo $TELEINFO | jq '.BBRHCJW')
BBRHPJW=$(echo $TELEINFO | jq '.BBRHPJW')
BBRHCJR=$(echo $TELEINFO | jq '.BBRHCJR')
BBRHPJR=$(echo $TELEINFO | jq '.BBRHPJR')
PTEC=$(echo $TELEINFO | jq '.PTEC')

if [ -z $PAPP ] || \
   [ -z $DEMAIN ] || \
   [ -z $BBRHCJB ] || \
   [ -z $BBRHPJB ] || \
   [ -z $BBRHCJW ] || \
   [ -z $BBRHPJW ] || \
   [ -z $BBRHCJR ] || \
   [ -z $BBRHPJR ]
   then
  echo "something went wrong reading data... exiting."
  exit 1
fi

ENERGY=$(($BBRHCJB+$BBRHPJB+$BBRHCJW+$BBRHPJW+$BBRHCJR+$BBRHPJR-$INITIALVALUE))

#echo "string:" $TELEINFO

#remove double quotes
DEMAIN=$(eval echo $DEMAIN)
PTEC=$(eval echo $PTEC)

send_data()
{
  dev=$1
  data1=$2
  data2=$3

  if [ ! "$data2" = "" ]
  then
    echo "sending data $data1,$data2 to dev $dev"
    if [ -z $TEST ] ; then
       curl -k "https://$DOMOTICZSRV/json.htm?type=command&param=udevice&idx=$dev&svalue=$data1;$data2"
    fi
  else if [ ! "$data1" = "" ]
    then
      echo "sending data $data1 to dev $dev"
      if [ -z $TEST ] ; then
        curl -k "https://$DOMOTICZSRV/json.htm?type=command&param=udevice&idx=$dev&svalue=$data1"
      fi
    fi
  fi
}

send_data $dev_papp "$PAPP" "$ENERGY"
send_data $dev_tarif "$PTEC"
send_data $dev_tarif_demain "$DEMAIN"
