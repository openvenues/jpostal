#!/usr/bin/env bash
./bootstrap.sh
./configure --libdir=$(pwd)/src/main/jniLibs
make install