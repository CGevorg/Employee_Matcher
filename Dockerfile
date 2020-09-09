FROM maven:3.6.3-jdk-11-slim as build
LABEL maintainer="CGevorgHarutyunyan@gmail.com"

COPY . /home/src
WORKDIR /home/src
RUN mvn package -Dmaven.test.skip -P prod

FROM openjdk:11-jre-slim
COPY --from=build /home/src/target/Matcher-0.0.1-SNAPSHOT.jar /home/app/matcher.jar
EXPOSE 8081:8081
ENTRYPOINT ["java", "-jar", "/home/app/matcher.jar"]