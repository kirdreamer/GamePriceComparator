FROM maven:3.9.6-eclipse-temurin-21-alpine
WORKDIR /app
COPY . /app
RUN mvn clean package -DskipTests
CMD ["java", "-jar", "target/GamePriceComparator-1.0-SNAPSHOT.jar"]