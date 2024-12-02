# Sử dụng base image với Java 17
FROM eclipse-temurin:17-jdk

# Đặt thư mục làm việc trong container
WORKDIR /app

# Copy file JAR đã build vào container
COPY build/libs/digital-blog-*.jar app.jar

# Expose cổng 8084
EXPOSE 8084

# Lệnh chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
