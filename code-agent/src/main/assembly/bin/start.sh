#!/bin/bash

# 定义一些变量
SERVER_NAME='code-agent'
JAR_FILE="$SERVER_NAME.jar"
JAVA_OPTS="-server -Xmx2048m -Xms1024m -Xmn1024m" # 你的 JVM 参数

LOG_DIR=./logs
PID_FILE=./logs/${SERVER_NAME}.pid

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
    nohup "java" $JAVA_OPTS -jar $DEPLOY_DIR/lib/$JAR_FILE > "$LOG_DIR/$SERVER_NAME.log" 2>&1 &
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