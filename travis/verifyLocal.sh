#!/usr/bin/env bash

set -e

#
# verify travis build locally
#

cd "${BASH_SOURCE%/*}/.."

echo "# workdir=$(pwd)"

scalaVersionList=(
   "2.11.12"
)

for scalaVersion in "${scalaVersionList[@]}" ; do
    export TRAVIS_SCALA_VERSION="$scalaVersion"
    travis/test.sh
done
