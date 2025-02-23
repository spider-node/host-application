#!/bin/bash

# 定义一些变量
SERVER_NAME='host-application'
JAR_FILE="$SERVER_NAME.jar"
JAVA_OPTS=" -server -Xmx2048m -Xms1024m -Xmn718m -Xss256k -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/applogs/system/error.dump" # 你的 JVM 参数

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
    nohup "java" $JAVA_OPTS -jar $DEPLOY_DIR/$JAR_FILE > $STDOUT_FILE 2>&1 &
    echo $! > "$PID_FILE"
    echo "Application started with PID $(cat "$PID_FILE")"
    tail -f $DEPLOY_DIR/logs/stdout.log
}

# 主函数
main() {
    check_running
    start_app
}

# 执行主函数
main