# Utilise un JDK léger pour Spring Boot
FROM eclipse-temurin:17-jdk-alpine

# Limite la RAM pour Render free tier
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Crée le dossier de travail
WORKDIR /app

# Copie Maven Wrapper et pom.xml pour cacher les dépendances
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Rends le Maven Wrapper exécutable
RUN chmod +x mvnw

# Télécharge les dépendances
RUN ./mvnw dependency:go-offline -B

# Copie le code source
COPY src ./src

# Build de l’application (skip tests pour accélérer)
RUN ./mvnw clean package -DskipTests

# Expose le port (Render injecte $PORT automatiquement)
EXPOSE 10000

# Commande de démarrage
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -Dserver.port=$PORT -jar target/*.jar"]