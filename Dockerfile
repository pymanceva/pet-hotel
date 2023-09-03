FROM bellsoft/liberica-openjdk-alpine:17
COPY *.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]