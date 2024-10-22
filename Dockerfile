# 1. Gradle 이미지를 사용하여 빌드
FROM gradle:7.5.1-jdk-alpine AS build
WORKDIR /app

# 2. Gradle 파일을 복사하여 의존성 다운로드
COPY build.gradle settings.gradle /app/
RUN gradle dependencies --no-daemon || true

# 3. 환경 파일 및 소스 파일 복사
# env.properties 파일을 복사하여 빌드에 포함
COPY env.properties /app/src/main/resources/application-secret.properties
COPY env.properties /app/src/test/resources/application-secret.properties

# 나머지 프로젝트 파일 복사
COPY . .

# 4. Gradle 빌드 실행
RUN gradle clean build --no-daemon --stacktrace

# 5. 최종 이미지를 위한 OpenJDK
FROM openjdk:17-slim

# 6. 빌드된 jar 파일 복사
COPY --from=build /app/build/libs/gonggibap-0.0.1-SNAPSHOT.jar app.jar

# 7. 환경 파일을 복사 (최종 이미지를 위한 설정 파일 복사)
COPY env.properties /config/application-secret.properties
COPY rds.yaml /config/rds.yaml

# 8. Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app.jar",
    "--spring.config.additional-location=/config/application-secret.properties,/config/rds.yaml"]