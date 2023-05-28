FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY build/libs/* build/lib/

COPY build/libs/TodoTaskService-0.0.1-SNAPSHOT.jar build/

WORKDIR /app/build
ENTRYPOINT java -jar TodoTaskService-0.0.1-SNAPSHOT.jar