# Construir imagem da aplicação para Deploy
FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-21-jdk -y

# Copiar tudo pro handler
COPY . .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:21-jdk-slim

# Rodar a aplicação na porta 8080
EXPOSE 8080

COPY --from=build /target/todolist-1.0.0.jar app.jar

# Comandos executáveis na linha de comando
ENTRYPOINT [ "java", "-jar", "app.jar" ]
