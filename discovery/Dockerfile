ARG SERVICE_NAME=discovery

FROM openjdk:17-jdk-alpine as builder

WORKDIR /app/discovery

ARG SERVICE_NAME

COPY ./pom.xml /app
COPY ./${SERVICE_NAME}/.mvn ./.mvn
COPY ./${SERVICE_NAME}/mvnw .
COPY ./${SERVICE_NAME}/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./${SERVICE_NAME}/src ./src

RUN ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-alpine

WORKDIR /app

ARG SERVICE_NAME
ARG TARGET_FOLDER=/app/${SERVICE_NAME}/target

RUN mkdir ./logs

COPY --from=builder ${TARGET_FOLDER}/discovery-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=9092

ENV PORT ${PORT_APP}

EXPOSE ${PORT}

CMD ["java", "-jar", "discovery-0.0.1-SNAPSHOT.jar"]
