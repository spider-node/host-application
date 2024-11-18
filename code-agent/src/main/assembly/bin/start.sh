#!/bin/bash

# 检查是否为root用户，如果不是则提示并退出
if [ "$EUID" -ne 0 ]; then
  echo "请以root权限运行此脚本"
  exit
fi

# 检查Maven是否已安装
if command -v mvn &> /dev/null; then
    echo "Maven 已经安装！"
else
    # 更新软件包列表
    echo "更新软件包列表..."
    apt-get update -y

    # 安装Maven
    echo "安装Maven..."
    apt-get install -y maven

    # 再次检查Maven是否安装成功
    if ! command -v mvn &> /dev/null; then
        echo "Maven 安装失败，请检查错误信息！"
        exit 1
    else
        echo "Maven 安装成功！"
    fi
fi

# 创建或修改settings.xml文件以使用阿里云的Maven镜像
SETTINGS_XML="/usr/share/maven/conf/settings.xml"
BACKUP_SETTINGS_XML="$SETTINGS_XML.bak"

# 备份原始的settings.xml文件
if [ -f "$SETTINGS_XML" ]; then
    cp "$SETTINGS_XML" "$BACKUP_SETTINGS_XML"
    echo "备份settings.xml到 $BACKUP_SETTINGS_XML"
fi

# 添加阿里云Maven镜像
cat <<EOF >"$SETTINGS_XML"
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">
  <mirrors>
    <mirror>
      <id>aliyun</id>
      <name>Aliyun Maven</name>
      <url>https://maven.aliyun.com/repository/public</url>
      <mirrorOf>*</mirrorOf>
    </mirror>
  </mirrors>
</settings>
EOF

echo "已将阿里云Maven镜像添加到settings.xml"

# 验证设置
mvn help:effective-settings | grep aliyun
if [ $? -eq 0 ]; then
    echo "阿里云Maven镜像配置成功！"
else
    echo "阿里云Maven镜像配置失败，请检查settings.xml文件！"
    exit 1
fi

echo "所有步骤完成，Maven安装及阿里云源配置完毕！"

# 验证设置
mvn help:effective-settings | grep aliyun
if [ $? -eq 0 ]; then
    echo "阿里云Maven镜像配置成功！"
else
    echo "阿里云Maven镜像配置失败，请检查settings.xml文件！"
    exit 1
fi

echo "所有步骤完成，Maven安装及阿里云源配置完毕！"


# 定义一些变量
SERVER_NAME='code-agent-1.0.0-SNAPSHOT'
JAR_FILE="$SERVER_NAME.jar"
JAVA_OPTS="-server -Xmx512m -Xms200m -Xmn70m" # 你的 JVM 参数

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
    cd $SHLLE_DIR
    chmod 755 generate_project.sh
    chmod 755 mkdir_area_base_path.sh
    chmod 755 run_mvn_install.sh
}

# 主函数
main() {
    check_running
    start_app
}

# 执行主函数
main