# Gradle 이미지를 사용합니다.
FROM gradle:7.5.1-jdk-alpine AS build
WORKDIR /app

# Gradle 파일 복사
COPY build.gradle settings.gradle /app/
RUN gradle dependencies --no-daemon || true

# 나머지 프로젝트 파일 복사
COPY ../dafs ./
RUN gradle clean build --no-daemon --stacktrace

# 최종 이미지 설정
FROM openjdk:17-slim

# 빌드된 jar 파일 복사
COPY --from=build /app/build/libs/gonggibap-0.0.1-SNAPSHOT.jar app.jar

# config 파일을 이미지에 복사
COPY config/env.properties /config/env.properties

# Spring Boot 실행 시 config 파일 참조
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.additional-location=/config/env.properties"]
