FROM openjdk:17-slim-bullseye AS builder
WORKDIR /app
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src/main ./src/main
RUN ./gradlew bootJar

FROM openjdk:17-slim-bullseye
WORKDIR /app
COPY --from=builder /app/build/libs/autoCommitWithCrawling-0.0.1-SNAPSHOT.jar app.jar

#docker chrom 설치
RUN apt-get update \
    && apt-get install -y sudo wget
RUN apt -y install gnupg gnupg1 gnupg2

RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
 && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list
RUN apt-get update
RUN apt-get -y install google-chrome-stable
#docker chrom 설치 /

#ENV PROFILE="dev"
ENV TZ=Asia/Seoul
ENTRYPOINT java -jar app.jar
#ENTRYPOINT java -jar app.jar --spring.profiles.active=$PROFI