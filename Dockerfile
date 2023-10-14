# # Construir imagem da aplicação para Deploy
# FROM ubuntu:latest AS build

# RUN apt-get update
# RUN apt-get install openjdk-17-jdk -y

# # Copiar tudo pro handler
# COPY . .

# RUN apt-get install maven -y
# RUN mvn clean install

# FROM openjdk:17-jdk-slim

# # Rodar a aplicação na porta 8080
# EXPOSE 8080

# COPY --from=build /target/todolist-1.0.0.jar app.jar

# # Comandos executáveis na linha de comando
# ENTRYPOINT [ "java", "-jar", "app.jar" ]



# CHATGPT:
# Use a imagem oficial do Java como imagem base
FROM openjdk:8-jdk-alpine

# Diretório de trabalho dentro do contêiner
WORKDIR /app

# Copie o arquivo JAR do seu projeto Spring Boot para o contêiner
COPY target/todolist-1.0.0.jar /app/todolist-1.0.0.jar

# Expõe a porta que seu aplicativo Spring Boot usa (geralmente 8080)
EXPOSE 8080

# Comando para executar o aplicativo Spring Boot quando o contêiner for iniciado
CMD ["java", "-jar", "todolist-1.0.0.jar"]