# Use the official OpenJDK image with Alpine Linux as the base image
FROM mcr.microsoft.com/openjdk/jdk:21-mariner

# Copy all JAR files from the updated context path
COPY build/libs/hazelcast-server-*.jar /app/

# Use a shell command to find the JAR file that doesn't contain '-plain' in the filename
# and rename it to a consistent name
RUN mv $(find /app/ -name "hazelcast-server-*.jar" ! -name "*-plain.jar") /app/hazelcast-server.jar

# Define the entry point for the container to run the selected JAR file using Java
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=dev", "/app/hazelcast-server.jar"]

# docker build -t hzservertest .
