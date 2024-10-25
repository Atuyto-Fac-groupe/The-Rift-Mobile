# Utilise une image de base Java OpenJDK
FROM openjdk:21

# Définit le répertoire de travail dans le conteneur
WORKDIR /app

# Copie l'application Java dans le conteneur
COPY "/build/libs/Serveur-0.0.1-SNAPSHOT.jar" /app/Server.jar

# Expose le port que le serveur utilise (remplacez 8080 par le port de votre serveur)
EXPOSE 9001

# Commande pour exécuter l'application Java
CMD ["java", "-jar", "/app/Server.jar"]
