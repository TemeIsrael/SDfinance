FROM maven:3.9.6-eclipse-temurin-21 AS build
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
# On utilise un script shell pour trouver le bon JAR exécutable Spring Boot
COPY --from=build /app/${SERVICE_NAME}/target/ /tmp/jars/
RUN JAR_FILE=$(ls /tmp/jars/*.jar | grep -v '.original' | head -1) && \
    cp "$JAR_FILE" /app/app.jar && \
    rm -rf /tmp/jars

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
