FROM adoptopenjdk/openjdk11:x86_64-alpine-jre-11.0.6_10
LABEL maintainer lindynetech@gmail.com
COPY build/libs/calculator-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]