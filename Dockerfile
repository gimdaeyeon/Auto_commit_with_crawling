FROM openjdk:17-slim-bullseye AS builder
WORKDIR /app
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src/main ./src/main
RUN ./gradlew bootJar

FROM openjdk:17-slim-bullseye
WORKDIR /app
COPY --from=builder /app/build/libs/autoCommitWithCrawling-0.0.1-SNAPSHOT.jar app.jar

ENV PROFILE="dev"
ENV TZ=Asia/Seoul
ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFILE