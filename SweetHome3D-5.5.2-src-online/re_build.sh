#!/bin/bash
clear
clear
clear
kill -9 `ps -ef|grep ZhongHang|grep -v grep|awk '{print $2}'`
ant
mv ./install/SweetHome3D-5.5.2.jar  ./install/ZhongHangHome3D-5.5.2.jar
java -jar ./install/ZhongHangHome3D-5.5.2.jar

