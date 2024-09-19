FROM maven:3-openjdk-17-slim AS build
WORKDIR /app

COPY ./pom.xml ./
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17 AS run
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /app/target/*.jar ./app.jar

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 CMD curl --fail http://localhost:8080/actuator/health || exit 1
EXPOSE 8080
ENTRYPOINT ["java","-jar","./app.jar"]