# running tests

This readme explains how to run tests for both Java 21 or Java 17.

Make sure everyone uses the same Maven version:

```bash
cd recruitment
./mvnw test
```

Notes
- Tests use a in memory H2 database, resides in `src/test/resources/application.properties`, so you don't need to run Postgres for tests.

Check java version with `java -version`

Running tests with Java 21:

```bash
# adjust this path if your JDK 21 is installed elsewhere
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
cd recruitment
./mvnw test
```

Running tests with Java 17:

```bash
# if you want to install jdk 17 (including this because all developers want to pivot and use same version)
sudo apt update
sudo apt install openjdk-17-jdk

#use jdk 17 and run maven tests for recruitment folder
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
cd recruitment
./mvnw test
```

Running a single test

```bash
#works by using -Dtest flag
cd recruitment
./mvnw -Dtest=ExampleUnitTest test
```