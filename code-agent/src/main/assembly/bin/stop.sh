#!/bin/bash
# 定义一些变量
APP_NAME=code-agent
PID_FILE=./logs/${APP_NAME}.pid

# 检查进程是否已经在运行
check_running() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if ps -p $PID > /dev/null; then
            echo "Stopping application with PID $PID..."
            kill $PID
            for i in {1..10}; do
                if ! ps -p $PID > /dev/null; then
                    echo "Application stopped."
                    rm "$PID_FILE"
                    exit 0
                fi
                sleep 1
            done
            echo "Application did not stop gracefully. Killing forcefully."
            kill -9 $PID
            rm "$PID_FILE"
            exit 1
        else
            echo "Application is not running."
            rm "$PID_FILE"
            exit 1
        fi
    else
        echo "Application is not running."
        exit 1
    fi
}

# 主函数
main() {
    check_running
}

# 执行主函数
main