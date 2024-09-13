FROM ubuntu:22.04

SHELL ["/bin/bash", "-c"]

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    unzip \
    software-properties-common \
    && wget -O - https://packages.adoptium.net/artifactory/api/gpg/key/public | apt-key add - \
    && add-apt-repository --yes https://packages.adoptium.net/artifactory/deb/ \
    && apt-get update && apt-get install -y temurin-17-jdk \
    && rm -rf /var/lib/apt/lists/*

ENV JAVA_HOME=/usr/lib/jvm/temurin-17-jdk-arm64
ENV PATH=$JAVA_HOME/bin:$PATH

RUN wget https://services.gradle.org/distributions/gradle-8.10.1-bin.zip \
    && mkdir /opt/gradle \
    && unzip -d /opt/gradle/ gradle-8.10.1-bin.zip \
    && rm -rf gradle-8.10.1-bin.zip

ENV PATH=$PATH:/opt/gradle/gradle-8.10.1/bin

WORKDIR /app/kafka-java-getting-started

ADD build.gradle .env ./
COPY src src

CMD ["/bin/bash"]
