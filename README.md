jpostal
-------

[![Build Status](https://travis-ci.org/openvenues/jpostal.svg?branch=master)](https://travis-ci.org/openvenues/jpostal)

These are the Java/JNI bindings to [libpostal](https://github.com/openvenues/libpostal), a fast, multilingual NLP library (written in C) for parsing/normalizing physical addresses around the world.

Usage
-----

To expand address strings into normalized forms suitable for geocoder queries:

```java
import com.mapzen.jpostal.AddressExpander;

// Singleton, libpostal setup is done in the constructor
AddressExpander e = AddressExpander.getInstance();
String[] expansions = e.expandAddress("Quatre vingt douze Ave des Champs-Élysées");
```

To parse addresses into components:

```java
import com.mapzen.jpostal.AddressParser;

// Singleton, parser setup is done in the constructor
AddressParser p = AddressParser.getInstance();
ParsedComponent[] components = p.parseAddress("The Book Club 100-106 Leonard St, Shoreditch, London, Greater London, EC2A 4RH, United Kingdom");

for (ParsedComponent c : components) {
    System.out.printf("%s: %s\n", c.getLabel(), c.getValue());
}
```

To use a libpostal installation with a datadir known at setup-time:

```java

import com.mapzen.jpostal.AddressParser;
import com.mapzen.jpostal.AddressExpander;

AddressExpander e = AddressExpander.getInstanceDataDir("/some/path");
AddressParser p = AddressParser.getInstanceDataDir("/some/path");
```

Building libpostal
------------------

Before building the Java bindings, you must install the libpostal C library. Make sure you have the following prerequisites:

**On Ubuntu/Debian**
```
sudo apt-get install curl autoconf automake libtool pkg-config
```

**On CentOS/RHEL**
```
sudo yum install curl autoconf automake libtool pkgconfig
```

**On Mac OSX**
```
sudo brew install curl autoconf automake libtool pkg-config
```

**Installing libpostal**

```
git clone https://github.com/openvenues/libpostal
cd libpostal
./bootstrap.sh
./configure --datadir=[...some dir with a few GB of space...]

# On Linux, set PKG_CONFIG_PATH to libpostal local repo root for the jpostal build follows.
export PKG_CONFIG_PATH=$(pwd)

make
sudo make install

# On Linux it's probably a good idea to run
sudo ldconfig
```

Note: libpostal >= v0.3.3 is required to use this binding.

Building jpostal
----------------

Only one command is needed:

```
./gradlew assemble
```

This will implicitly run [build.sh](./build.sh) which automatically runs the Autotools build for the JNI/C portion of the library and installs the resulting shared libraries in the expected location for java.library.path

On RHEL it might be necessary to set ```PKG_CONFIG_PATH=/usr/lib/pkgconfig``` before running the gradle build.

Usage in a Java project
-----------------------

The JNI portion of jpostal builds shared object files (.so on Linux, .jniLib on Mac) that need to be on java.library.path. After running ```gradle assemble``` the .so/.jniLib files can be found under ```src/main/jniLibs``` in the checkout dir. For running the tests, we set java.library.path explicitly [here](https://github.com/openvenues/jpostal/blob/master/build.gradle#L18).

For gradle users, there's a plugin called gradle-natives that may be helpful: https://github.com/cjstehno/coffeaelectronica/wiki/Going-Native-with-Gradle

Compatibility
-------------

Building jpostal is known to work on Linux and Mac OSX. The Travis CI build tests Oracle JDK 7/8 and OpenJDK 7.

Tests
-----

To run the tests:

```
./gradlew check
```

License
-------

The package is available as open source under the terms of the [MIT License](http://opensource.org/licenses/MIT).
