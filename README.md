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

Building libpostal
------------------

Before building the Java bindings, you must install the libpostal C library. Make sure you have the following prerequisites:

**On Ubuntu/Debian**
```
sudo apt-get install curl libsnappy-dev autoconf automake libtool pkg-config
```

**On CentOS/RHEL**
```
sudo yum install snappy snappy-devel autoconf automake libtool pkgconfig
```

**On Mac OSX**
```
sudo brew install snappy autoconf automake libtool pkg-config
```

**Installing libpostal**

```
git clone https://github.com/openvenues/libpostal
cd libpostal
./bootstrap.sh
./configure --datadir=[...some dir with a few GB of space...]
make
sudo make install

# On Linux it's probably a good idea to run
sudo ldconfig
```

Building jpostal
----------------

Only one command is needed:

```
./gradlew assemble
```

This will implicitly run [build.sh](./build.sh) which automatically runs the Autotools build for the JNI/C portion of the library and installs the resulting shared libraries in the expected location for java.library.path

On RHEL it might be necessary to set ```PKG_CONFIG_PATH=/usr/lib/pkgconfig``` before running the gradle build.

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
