FROM openjdk:17-jdk-slim

WORKDIR /app

COPY pom.xml .

RUN apt-get update && apt-get install -y maven \
    && mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/EECToronto-0.0.1-SNAPSHOT.jar"]