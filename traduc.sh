#!/bin/sh
p=$(pwd)
echo $p
echo $0
echo $(basename $0)
java -jar $p/traduc.jar

