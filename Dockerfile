# Etapa 1: Build da aplicação Spring Boot
# MUDADO: Imagem do Maven agora usa Eclipse Temurin com JDK 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Execução da aplicação
# MUDADO: Imagem de execução agora usa Eclipse Temurin JRE 21
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]