FROM amazoncorretto:22-alpine

LABEL "com.gmc.navin3d"="smnavin65@gmail.com"

LABEL version="0.1"

WORKDIR /usr/app

COPY ./target/*.jar ./app.jar
COPY ./Financial_Data ./Financial_Data

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]
