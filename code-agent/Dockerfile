FROM eclipse-temurin:8-jdk
RUN mkdir /usr/local/spider-agent/
COPY /target/code-agent.tar.gz /usr/local/spider-agent/
WORKDIR /usr/local/spider-agent/
RUN tar -zxvf code-agent.tar.gz
ENTRYPOINT ["/bin/bash", "-c", "/usr/local/spider-agent/bin/start.sh"]