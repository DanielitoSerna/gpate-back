FROM amazoncorretto:11-alpine-jdk
MAINTAINER gpate
COPY target/gpate-0.0.1-SNAPSHOT.jar  gpate.jar

RUN apk add --no-cache msttcorefonts-installer fontconfig
RUN update-ms-fonts

ENTRYPOINT ["java","-jar","/gpate.jar"]