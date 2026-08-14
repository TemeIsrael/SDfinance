FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copier tous les fichiers du projet (le .dockerignore exclut les target/)
COPY . .

# Compiler tout le projet
RUN ./mvnw clean package -DskipTests || mvn clean package -DskipTests

# Etape finale : execution
FROM eclipse-temurin:21-jre
WORKDIR /app

# L'argument qui détermine quel service lancer
ARG SERVICE_NAME

# Copier le .jar généré depuis l'étape de build
COPY --from=build /app/${SERVICE_NAME}/target/*.jar app.jar

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
