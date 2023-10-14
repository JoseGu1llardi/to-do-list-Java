FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

COPY . .

RUN apt-get install maven -y
RUN mvn clean install

EXPOSE 3333

COPY --from=build /target/to-do-list-1.0.0-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]