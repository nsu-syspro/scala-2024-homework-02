#!/usr/bin/env sh

if [ ! -d "classes" ]; then
    mkdir classes
fi
scalac -d classes paddle.scala
scala -classpath classes sanity_check.scala
