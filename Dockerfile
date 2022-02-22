FROM gradle:jdk8-alpine as builder

COPY --chown=gradle:gradle ./build.gradle /home/gradle/
COPY --chown=gradle:gradle ./settings.gradle /home/gradle/
COPY --chown=gradle:gradle ./src /home/gradle/src
RUN gradle build -Pbuilddir=build

FROM openjdk:8-jre-alpine

RUN addgroup -S -g 1000 app \
    && adduser -D -S -G app -u 1000 -s /bin/ash app
USER app
WORKDIR /home/app
COPY --from=builder --chown=app:app /home/gradle/build/libs/cowweb-1.0.jar .
CMD ["java", "-jar", "/home/app/cowweb-1.0.jar"]