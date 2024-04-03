FROM openjdk:17-slim-bullseye AS builder
WORKDIR /app
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src/main ./src/main
RUN ./gradlew bootJar

FROM openjdk:17-slim-bullseye
WORKDIR /app
COPY --from=builder /app/build/libs/auto_commit_with_crawling-*.jar app.jar

ENV PROFILE="dev"
ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE

