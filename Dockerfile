FROM openjdk:11-jdk
EXPOSE 8080
ADD target/reading-is-good.jar reading-is-good.jar
ENTRYPOINT ["java","-jar","/reading-is-good.jar"]
