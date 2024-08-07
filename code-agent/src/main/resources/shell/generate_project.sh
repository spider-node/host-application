#!/bin/bash

# 检查参数数量

# 项目名称 参照 GoodsOrderUpdateService
PROJECT_DIR="$1"
# pom中的 GROUP_ID cn.spider
GROUP_ID="$2"
# ARTIFACT_ID 参照 table名称GoodsOrderUpdateService
ARTIFACT_ID="$3"
# 版本
VERSION="$4"
# 分组拆分
GROUP_PATH="$5"

PROJECT_FINAL="$6"

JAVA_FILE_PATH="$7"

mkdir -p $PROJECT_FINAL
# 删除旧项目，如果存在的话
cd "$PROJECT_FINAL"


if [ -d "$ARTIFACT_ID" ]; then
    # 如果目录存在，使用 'rm' 命令删除目录及其内容
    cd $ARTIFACT_ID
    mvn clean
    cd "../"
    rm -rf "$ARTIFACT_ID"
    echo "Directory $ARTIFACT_ID has been deleted."
else
    # 如果目录不存在，输出一条消息
    echo "Directory $ARTIFACT_ID does not exist."
fi

# 使用Maven archetype生成项目
mvn archetype:generate \
    -DarchetypeGroupId=org.apache.maven.archetypes \
    -DarchetypeArtifactId=maven-archetype-quickstart \
    -DgroupId=$GROUP_ID \
    -DartifactId=$ARTIFACT_ID \
    -Dversion=$VERSION \
    -Dpackage=$GROUP_ID \
    -DinteractiveMode=false

# 创建spider方法的存放路径
mkdir -p "$PROJECT_FINAL/$PROJECT_DIR/src/main/java/$GROUP_PATH/$JAVA_FILE_PATH/spider/service"
# 创建spider方法的入参，出参方法路径
mkdir -p "$PROJECT_FINAL/$PROJECT_DIR/src/main/java/$GROUP_PATH/$JAVA_FILE_PATH/spider/data"
# 创建 spider配置类路径
mkdir -p "$PROJECT_FINAL/$PROJECT_DIR/src/main/java/$GROUP_PATH/$JAVA_FILE_PATH/config"
# 创建spider的resource中的，配置文件路口
mkdir -p "$PROJECT_FINAL/$PROJECT_DIR/src/main/resources"