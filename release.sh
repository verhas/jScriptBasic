#!/usr/bin/env bash

echo copiing release files
rm -rf release
mkdir -p release
cp target/*.jar release/
cp pom.xml release/

cd release

for file in *.jar pom.xml
do
    echo Signing ${file}
    gpg -s -b ${file}
    mv ${file}.sig ${file}.asc
done

echo Creating release.zip
jar -c -M -f release.zip *.jar pom.xml *.asc

cd ..
echo done.
