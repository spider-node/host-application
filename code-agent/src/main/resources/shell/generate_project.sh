#!/bin/bash

# 检查参数数量

# 项目名称 参照 GoodsOrderUpdateService
PROJECT_DIR="$0"
# pom中的 GROUP_ID cn.spider
GROUP_ID="$1"
# ARTIFACT_ID 参照 table名称GoodsOrderUpdateService
ARTIFACT_ID="$2"
# 项目的根目录
ROOT_DIR="$3"
# 分组拆分
GROUP_PATH="$4"

VERSION="$5"

AREA_NAME="$6"

PROJECT_FINAL="$ROOT_DIR/$AREA_NAME/$PROJECT_DIR/$VERSION"
mkdir -p $PROJECT_FINAL
# 删除旧项目，如果存在的话
cd "$PROJECT_FINAL"
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
mkdir -p "$PROJECT_FINAL/$PROJECT_DIR/src/main/java/$GROUP_PATH/$PROJECT_DIR/spider/service"
# 创建spider方法的入参，出参方法路径
mkdir -p "$PROJECT_FINAL/$PROJECT_DIR/src/main/java/$GROUP_PATH/$PROJECT_DIR/spider/data"
# 创建 spider配置类路径
mkdir -p "$PROJECT_FINAL/$PROJECT_DIR/src/main/java/$GROUP_PATH/$PROJECT_DIR/config"
# 创建spider的resource中的，配置文件路口
mkdir -p "$PROJECT_FINAL/$PROJECT_DIR/src/main/resources"