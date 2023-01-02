ARG MSVC_NAME=msvc-libros

FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/msvc-libros

ARG MSVC_NAME

COPY ./pom.xml /app

COPY ./${MSVC_NAME}/.mvn ./.mvn
COPY ./${MSVC_NAME}/mvnw .
COPY ./${MSVC_NAME}/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./${MSVC_NAME}/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

ARG MSVC_NAME
ARG TARGET_FOLDER=/app/${MSVC_NAME}/target

RUN mkdir ./logs

COPY --from=builder ${TARGET_FOLDER}/msvc-libros-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8000

ENV PORT ${PORT_APP}

EXPOSE ${PORT}
CMD ["java", "-jar", "msvc-libros-0.0.1-SNAPSHOT.jar"]
