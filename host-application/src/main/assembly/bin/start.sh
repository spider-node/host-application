#!/bin/bash

# 定义一些变量
SERVER_NAME='host-application-1.0.0'
JAR_FILE="$SERVER_NAME.jar"
JAVA_OPTS=" -server -Xmx3096m -Xms2048m -Xmn1024m -XX:PermSize=256m -Xss256k -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/applogs/system/error.dump -koupleless.arklet.http.port=8083" # 你的 JVM 参数

cd ..
DEPLOY_DIR=`pwd`

LOG_DIR=./logs
STDOUT_FILE=$LOG_DIR/stdout.log

PID_FILE=./logs/${SERVER_NAME}.pid

SHLLE_DIR=./conf/shell

# 创建日志目录
mkdir -p "$LOG_DIR"


# 检查进程是否已经在运行
check_running() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p $PID > /dev/null; then
            echo "Application is already running with PID $PID"
            exit 1
        else
            rm "$PID_FILE"
        fi
    fi
}

# 启动应用
start_app() {
    echo "Starting $SERVER_NAME..."
    nohup "java" $JAVA_OPTS -jar $DEPLOY_DIR/lib/$JAR_FILE > $STDOUT_FILE 2>&1 &
    echo $! > "$PID_FILE"
    echo "Application started with PID $(cat "$PID_FILE")"
}

# 主函数
main() {
    check_running
    start_app
}

# 执行主函数
main