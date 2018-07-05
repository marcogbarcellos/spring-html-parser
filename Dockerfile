# FROM java:8
# ADD spring-html-parser-0.1.0.jar app.jar
# RUN bash -c 'touch /app.jar'
# ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongo/test", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

FROM java:8

RUN mkdir /app
COPY . /app
WORKDIR /app
# USER root
RUN ./gradlew build
#ADD /build/libs/spring-html-parser-0.1.0.jar app.jar
#RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-jar","build/libs/spring-html-parser-0.1.0.jar"]