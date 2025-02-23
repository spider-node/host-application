#!/bin/bash

# 检查是否为root用户，如果不是则提示并退出
if [ "$EUID" -ne 0 ]; then
  echo "请以root权限运行此脚本"
  exit 1
fi

# 定义Maven版本及国内下载链接
MAVEN_VERSION="apache-maven-3.9.5"
MAVEN_URL="https://mirrors.aliyun.com/apache/maven/maven-3/3.9.5/binaries/${MAVEN_VERSION}-bin.tar.gz"
MAVEN_HOME="/opt/${MAVEN_VERSION}"
SETTINGS_XML="${MAVEN_HOME}/conf/settings.xml"
BACKUP_SETTINGS_XML="$SETTINGS_XML.bak"

# 检查Maven是否已安装
if command -v mvn &> /dev/null; then
    echo "Maven 已经安装！"
else
    # 下载Maven tar.gz包到临时文件夹，并检查下载是否成功
    echo "下载 Maven..."
    wget --no-check-certificate -qO /tmp/${MAVEN_VERSION}-bin.tar.gz $MAVEN_URL || { echo "Maven 下载失败，请检查网络连接和下载链接！"; exit 1; }

    # 解压到目标位置
    mkdir -p /opt
    tar -xzf /tmp/${MAVEN_VERSION}-bin.tar.gz -C /opt/
    rm /tmp/${MAVEN_VERSION}-bin.tar.gz # 清理下载的压缩包

    # 配置环境变量
    echo "配置Maven环境变量..."
    export PATH=$MAVEN_HOME/bin:$PATH
    echo "export MAVEN_HOME=$MAVEN_HOME" >> /etc/profile.d/maven.sh
    echo 'export PATH=$MAVEN_HOME/bin:$PATH' >> /etc/profile.d/maven.sh
    source /etc/profile.d/maven.sh

    mkdir -p /usr/local/spider-node
    cd /usr/local/spider-node
    wget https://spider-node.oss-cn-beijing.aliyuncs.com/spider-node/repository.tar.gz
    tar -xzf repository.tar.gz
    rm repository.tar.gz

    # 再次检查Maven是否安装成功
    if ! command -v mvn &> /dev/null; then
        echo "Maven 安装失败，请检查错误信息！"
        exit 1
    else
        echo "Maven 安装成功！"
    fi
fi

# 确保配置目录存在
mkdir -p "${MAVEN_HOME}/conf"

# 备份原始的settings.xml文件（如果存在）
if [ -f "$SETTINGS_XML" ]; then
    cp "$SETTINGS_XML" "$BACKUP_SETTINGS_XML"
    echo "备份settings.xml到 $BACKUP_SETTINGS_XML"
fi

# 添加阿里云Maven镜像
cd "${MAVEN_HOME}/conf"
# 下载 wget https://spider-node.oss-cn-beijing.aliyuncs.com/spider-node/settings.xml 替换settings.xml
curl https://spider-node.oss-cn-beijing.aliyuncs.com/spider-node/settings.xml > settings.xml

echo "已将阿里云Maven镜像添加到settings.xml"

# 通过网络下载https://spider-node.oss-cn-beijing.aliyuncs.com/spider-node/spider-node-community.tar.gz

echo "配置-环境必须的maven依赖完成"
# 验证设置
mvn help:effective-settings | grep aliyun
if [ $? -eq 0 ]; then
    echo "阿里云Maven镜像配置成功！"
else
    echo "阿里云Maven镜像配置失败，请检查settings.xml文件！"
    exit 1
fi

echo "所有步骤完成，Maven安装及阿里云源配置完毕！"

cd /usr/local/spider-agent


SERVER_NAME='code-agent-1.0.0-SNAPSHOT'
JAR_FILE="$SERVER_NAME.jar"
JAVA_OPTS="-server -Xmx512m -Xms200m -Xmn70m" # 你的 JVM 参数

DEPLOY_DIR=$(pwd)

LOG_DIR=./logs
STDOUT_FILE=$LOG_DIR/stdout.log
PID_FILE=./logs/${SERVER_NAME}.pid
SHLLE_DIR=/usr/local/spider-agent/conf/shell

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
    nohup java $JAVA_OPTS -jar $DEPLOY_DIR/lib/$JAR_FILE > $STDOUT_FILE 2>&1 &
    echo $! > "$PID_FILE"
    echo "Application started with PID $(cat "$PID_FILE")"
    cd $SHLLE_DIR
    chmod +x generate_project.sh mkdir_area_base_path.sh run_mvn_install.sh
    tail -f $DEPLOY_DIR/logs/stdout.log
}

# 主函数
main() {
    check_running
    start_app
}

# 执行主函数
main