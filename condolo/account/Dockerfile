FROM eclipse-temurin:21-jdk-alpine
RUN apk update && apk upgrade
EXPOSE 8091
ARG JAR_FILE=build/libs/account-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]