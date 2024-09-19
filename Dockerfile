FROM maven:3-openjdk-17-slim AS build
WORKDIR /app

COPY ./pom.xml ./
RUN mvn dependency:go-offline

COPY ./src ./src
RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17 AS run
WORKDIR /app

COPY --from=build /app/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","./app.jar"]