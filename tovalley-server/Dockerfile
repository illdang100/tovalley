FROM openjdk:11-jdk-slim

WORKDIR /app

# COPY만 docker-compose 파일의 위치를 기반으로 작동한다.
COPY . .

ENV JAR_PATH=/app/build/libs
RUN mv ${JAR_PATH}/*.jar /app/app.jar

ENTRYPOINT [ "java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=prod", "app.jar" ]