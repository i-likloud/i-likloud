FROM adoptopenjdk/openjdk11
LABEL authors="SSAFY"
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
