FROM node:22.8.0-alpine

RUN apk --no-cache add \
      bash \
      g++ \
      ca-certificates \
      lz4-dev \
      musl-dev \
      cyrus-sasl-dev \
      openssl-dev \
      make \
      python3

RUN apk add --no-cache --virtual .build-deps gcc zlib-dev libc-dev bsd-compat-headers py-setuptools bash

WORKDIR /app

RUN mkdir kafka-nodejs-getting-started \
    && cd kafka-nodejs-getting-started

WORKDIR /app/kafka-nodejs-getting-started

RUN npm install node-rdkafka dotenv

COPY .env ./
COPY src src
