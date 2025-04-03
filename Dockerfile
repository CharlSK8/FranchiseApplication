FROM gradle:jdk17-corretto-al2023 AS build

WORKDIR /app

COPY build.gradle settings.gradle /app/

COPY src /app/src

RUN gradle clean
RUN gradle build

FROM eclipse-temurin:17-jre-alpine

RUN apk add --no-cache bash

WORKDIR /app

COPY --from=build /app/build/libs/prueba-1.0.0.jar /app/FranchiseApp.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "FranchiseApp.jar"]