FROM ubuntu:22.04

RUN apt-get update && apt-get install -y python3.10 python3-venv python3-pip

WORKDIR /app/kafka-python-getting-started

COPY . .

RUN python3 -m venv .venv
ENV PATH="/app/kafka-python-getting-started/.venv/bin:$PATH"

RUN pip install -r requirements.txt

CMD ["/bin/bash"]
