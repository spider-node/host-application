FROM openjdk:8-jdk
RUN mkdir /usr/local/host-application/
COPY /target/host-application.jar /usr/local/host-application/
COPY start.sh /usr/local/host-application/bin/start.sh
WORKDIR /usr/local/host-application/
EXPOSE 8080
ENV JAVA_OPTS=" -server -Xmx2048m -Xms1024m -Xmn718m -Xss256k -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/applogs/system/error.dump"
ENV MODULE_CONTROLLER_ADDRESS=127.0.0.1:7777
CMD ["sh", "-c", "java -Dkoupleless.arklet.http.heartbeat.endpoint=http://${MODULE_CONTROLLER_ADDRESS} -Dsofa.ark.auto.uninstall.when.failed.enable=false -Dsofa.ark.activate.multi.biz.version.enable=true ${JAVA_OPTS} -jar host-application.jar"]