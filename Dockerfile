FROM adoptopenjdk/openjdk11 as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean install -DskipTests

FROM adoptopenjdk/openjdk11
COPY --from=build  /workspace/app/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]