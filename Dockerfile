FROM amazoncorretto:11-alpine-jdk
MAINTAINER gpate
COPY target/gpate-0.0.1-SNAPSHOT.jar  gpate.jar
ENTRYPOINT ["java","-jar","/gpate.jar"]