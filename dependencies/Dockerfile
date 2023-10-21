#
#  Install [libpostal](https://github.com/openvenues/libpostal) on a base image with a JDK
#   to support automated builds of the jpostal project
#
# Note: An image built with this dockerfile is available publicly on Dockerhub as qualytics42/qualytics-jpostal-base:0.1.0
#  built with `docker buildx build --platform linux/arm64,linux/amd64 .` on a M1 Mac
#
#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
FROM cimg/openjdk:17.0

USER 0

RUN set -ex; \
    mkdir -p /home; \
    cd /home; \
    apt-get update; \
    apt-get install -y build-essential curl autoconf automake libtool pkg-config git zip; \
    git clone https://github.com/openvenues/libpostal; \
    cd libpostal; \
    ./bootstrap.sh; \
    mkdir data_dir; \
#    ./configure --datadir=`pwd`/data_dir MODEL=senzing; \
    # sse2 is not available when building on M1 Mac
    ./configure --datadir=`pwd`/data_dir MODEL=senzing --disable-sse2; \
    make -j4; \
    make install; \
    chown -R circleci ../libpostal \
    ldconfig;

USER circleci
