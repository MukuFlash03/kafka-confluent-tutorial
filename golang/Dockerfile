FROM ubuntu:22.04

SHELL ["/bin/bash", "-c"]

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y wget gnupg unzip tar

RUN wget https://go.dev/dl/go1.21.0.linux-arm64.tar.gz \
    && rm -rf /usr/local/go \
    && tar -C /usr/local -xzf go1.21.0.linux-arm64.tar.gz \
    && rm -rf go1.21.0.linux-arm64.tar.gz

ENV PATH=$PATH:/usr/local/go/bin

WORKDIR /app/kafka-go-getting-started

ADD .env ./
COPY src src

RUN apt-get update && apt-get install -y build-essential

RUN go mod init kafka-go-getting-started \
    && go get github.com/confluentinc/confluent-kafka-go/kafka \
    && go get github.com/joho/godotenv \
    && go mod tidy

CMD ["/bin/bash"]
