#!/bin/bash
ant
ant -buildfile createJar.xml

java -cp runeoptimize.jar swrunes.ClearConfig

mkdir release
mkdir release/imgs
cp *.jar release/
cp config_temp.json release/config.json
cp optimizer.json release/
cp README.txt release/
cp imgs/* release/imgs/
cp runtool* release/
cp runPetM* release/

zip -r release.zip release

echo "Done"
