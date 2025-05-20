FROM eclipse-temurin:21-jdk-alpine

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o JAR gerado para o container
COPY target/gerador-horario-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta usada pela aplicação
EXPOSE 8080

# Comando que roda a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]