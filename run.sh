#!/bin/bash
# eclipse 에서 project 에서 export jar 로 만들어야 합니다.
mvn clean package -Dmaven.test.skip=true

# echo "cp ./target/busyBox-1.3.0.jar /mnt/d/PROJ/04.KRSMART/workspace/KRSMART_RENEWAL/src/main/resources/lib/"
