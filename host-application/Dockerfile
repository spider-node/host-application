FROM openjdk:8-jdk
RUN mkdir /usr/local/host-application/
COPY /target/host-application.jar /usr/local/host-application/
COPY start.sh /usr/local/host-application/bin/start.sh
WORKDIR /usr/local/host-application/
EXPOSE 8080
EXPOSE 1238
ENV JAVA_OPTS=" -server -Xmx2048m -Xms1024m -Xmn718m -Xss256k -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/applogs/system/error.dump"
ENV MODULE_CONTROLLER_ADDRESS=127.0.0.1:7777
CMD ["sh", "-c", "java -Dkoupleless.arklet.http.heartbeat.endpoint=http://${MODULE_CONTROLLER_ADDRESS} -Dsofa.ark.auto.uninstall.when.failed.enable=false -Dsofa.ark.activate.multi.biz.version.enable=true ${JAVA_OPTS} -jar host-application.jar"]
# CMD ["sh", "-c", "java ${JAVA_OPTS} -jar host-application.jar"]

# CMD ["sh", "-c", "java -Dkoupleless.arklet.mqtt.enable=true -Dkoupleless.arklet.mqtt.port=80 -Dkoupleless.arklet.mqtt.broker=mqtt-service.spider-vertx -Dkoupleless.arklet.mqtt.username=koupleless_base -Dkoupleless.arklet.mqtt.password=public -Dkoupleless.arklet.mqtt.client.prefix=koupleless -Dkoupleless.arklet.custom.tunnel.classname=com.alipay.sofa.koupleless.arklet.tunnel.mqtt.MqttTunnel ${JAVA_OPTS} -jar host-application.jar"]