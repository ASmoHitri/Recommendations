FROM openjdk:8-jre-slim
RUN mkdir /app
WORKDIR /app
ADD ./api/target/api-1.0.0-SNAPSHOT.jar /app
EXPOSE 8083
CMD ["java", "-jar","api-1.0.0-SNAPSHOT.jar"]
