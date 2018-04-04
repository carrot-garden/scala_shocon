#!/usr/bin/env bash

set -e

# Tricks to avoid unnecessary cache updates, from
# http://www.scala-sbt.org/0.13/docs/Travis-CI-with-sbt.html
  
find $HOME/.sbt -name "*.lock" | xargs rm
find $HOME/.ivy2 -name "ivydata-*.properties" | xargs rm
