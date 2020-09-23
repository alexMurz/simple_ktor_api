
# Build app
FROM openjdk:8 AS build

RUN mkdir /appbuild
COPY . /appbuild

WORKDIR /appbuild

RUN ./gradlew clean build

# Container setup
FROM openjdk:8-jre-alpine

# Creating user
ENV APPLICATION_USER ktor_srv_user
RUN adduser -D -g '' $APPLICATION_USER

# Create /app directory structure
RUN mkdir /app
RUN mkdir /app/resources

# /app ownership
RUN chown -R $APPLICATION_USER /app
# RW permissions for $APPLICATION_USER to /app
RUN chmod -R 755 /app

# Setting user to use when running the image
USER $APPLICATION_USER

# Copy jar
COPY --from=build /appbuild/build/libs/example*all.jar /app/server.jar
# Copy resources
COPY --from=build /appbuild/resources/ /app/resources/
WORKDIR /app

# Entry Point
CMD ["sh", "-c", "java -server -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:InitialRAMFraction=2 -XX:MinRAMFraction=2 -XX:MaxRAMFraction=2 -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+UseStringDeduplication -jar server.jar"]
