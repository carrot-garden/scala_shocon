#!/usr/bin/env bash

set -e

#
# verify root build
#

sbt -no-colors ++$TRAVIS_SCALA_VERSION test
