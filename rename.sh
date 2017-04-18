#!/bin/bash

sed -i "s/java-maven-travis/$1/g" pom.xml
sed -i "s/java-maven-travis/$1/g" README.md
sed -i "s/java-maven-travis/$1/g" .git/config

mv ../java-maven-travis ../$1 && cd ../$1