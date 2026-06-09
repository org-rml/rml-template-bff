FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Credenciais do GitHub Packages passadas como build-arg (não ficam na imagem final)
ARG GITHUB_ACTOR
ARG PACKAGES_TOKEN

# Cria settings.xml temporário com auth do GitHub Packages
RUN mkdir -p /root/.m2 && printf '\
<settings>\n\
  <servers>\n\
    <server>\n\
      <id>github</id>\n\
      <username>%s</username>\n\
      <password>%s</password>\n\
    </server>\n\
  </servers>\n\
</settings>\n' "$GITHUB_ACTOR" "$PACKAGES_TOKEN" > /root/.m2/settings.xml

COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Remove credenciais antes de finalizar o stage
RUN rm -f /root/.m2/settings.xml

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S rml && adduser -S rml -G rml
USER rml

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
