FROM bellsoft/liberica-openjdk-alpine:21-cds as builder
RUN mkdir /spring-app-spring
COPY . /spring-app-spring
WORKDIR /spring-app-spring
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /spring-app-spring

COPY --from=builder /spring-app-spring/target/*.jar /spring-app-spring/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/*.jar"]