#!/usr/bin/env bash

set -e

#
# verify plugin build
#

if [ $SBT_VERSION ]; then 
    
  sbt -no-colors ++$TRAVIS_SCALA_VERSION publishLocal
  
  cd plugin
  
  sbt -no-colors ^^$SBT_VERSION 'scripted shocon/basic'
  
fi
