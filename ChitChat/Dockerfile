# -------- Stage 1: Build --------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# -------- Stage 2: Run (Jakarta) --------
FROM tomcat:10.1-jdk17

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/HIWeb/target/HIWeb-0.0.1-SNAPSHOT.war \
    /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
