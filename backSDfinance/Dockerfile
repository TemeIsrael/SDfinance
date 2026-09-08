FROM eclipse-temurin:21-jre
WORKDIR /app

# L'argument qui détermine quel service lancer
ARG SERVICE_NAME

# Copier le .jar généré depuis l'hôte dans target/
COPY ${SERVICE_NAME}/target/*.jar /tmp/jars/

RUN JAR_FILE=$(ls /tmp/jars/*.jar | grep -v '.original' | head -1) && \
    cp "$JAR_FILE" /app/app.jar && \
    rm -rf /tmp/jars

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]
