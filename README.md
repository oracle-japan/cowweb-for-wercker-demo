# cowweb-helidon

![badge](https://github.com/oracle-japan/cowweb-helidon/actions/workflows/maven.yml/badge.svg)

Cowsay Web API.

## Build and run

With JDK17+

```bash
./mvnw package
java -jar target/cowweb-helidon.jar
```

## Exercise the application

cowsay's say.

```bash
curl -X GET http://localhost:8080/cowsay/say
 ______
< Moo! >
 ------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||

curl -X GET http://localhost:8080/cowsay/say?say=Hello
 _______
< Hello >
 -------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||

curl -X GET http://localhost:8080/cowsay/say?say=Wow!&cowfile=www
 _______
< Wow! >
 -------
        \   ^__^
         \  (oo)\_______
            (__)\       )\/\
                ||--WWW |
                ||     ||
```

cowsay's think.

```bash
curl -X GET http://localhost:8080/cowsay/think
 ______
( Moo! )
 ------
        o   ^__^
         o  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||

curl -X GET http://localhost:8080/cowsay/think?think=Hello
 _______
( Hello )
 -------
        o   ^__^
         o  (oo)\_______
            (__)\       )\/\
                ||----w |
                ||     ||

curl -X GET http://localhost:8080/cowsay/think?think=Wow!&cowfile=www
 ______
( Wow! )
 ------
        o   ^__^
         o  (oo)\_______
            (__)\       )\/\
                ||--WWW |
                ||     ||
```

## Try metrics

```bash
# Prometheus Format
curl -s -X GET http://localhost:8080/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/metrics
{"base":...
. . .
```

## Try health

```bash
curl -s -X GET http://localhost:8080/health
{"status":"UP","checks":[]}
```

## Building a Native Image

Make sure you have GraalVM locally installed:

```bash
$GRAALVM_HOME/bin/native-image --version
```

Build the native image using the native image profile:

```bash
./mvnw package -Pnative-image
```

This uses the helidon-maven-plugin to perform the native compilation using your installed copy of GraalVM. It might take a while to complete.
Once it completes start the application using the native executable (no JVM!):

```bash
./target/cowweb-helidon
```

Yep, it starts fast. You can exercise the application’s endpoints as before.

## Building the Docker Image

```bash
docker build -t cowweb-helidon .
```

## Running the Docker Image

```bash
docker run --rm -p 8080:8080 cowweb-helidon:latest
```

Exercise the application as described above.

## Building a Custom Runtime Image

Build the custom runtime image using the jlink image profile:

```bash
./mvn package -Pjlink-image
```

This uses the helidon-maven-plugin to perform the custom image generation.
After the build completes it will report some statistics about the build including the reduction in image size.

The target/cowweb-helidon-jri directory is a self contained custom image of your application. It contains your application,
its runtime dependencies and the JDK modules it depends on. You can start your application using the provide start script:

```bash
./target/cowweb-helidon-jri/bin/start
```

Class Data Sharing (CDS) Archive
Also included in the custom image is a Class Data Sharing (CDS) archive that improves your application’s startup
performance and in-memory footprint. You can learn more about Class Data Sharing in the JDK documentation.

The CDS archive increases your image size to get these performance optimizations. It can be of significant size (tens of MB).
The size of the CDS archive is reported at the end of the build output.

If you’d rather have a smaller image size (with a slightly increased startup time) you can skip the creation of the CDS
archive by executing your build like this:

```bash
./mvn package -Pjlink-image -Djlink.image.addClassDataSharingArchive=false
```

For more information on available configuration options see the helidon-maven-plugin documentation.
