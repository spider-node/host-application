#!/bin/bash

# 检查参数数量
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <project_dir> <group_id> <artifact_id>"
    exit 1
fi

# 获取参数
PROJECT_DIR="$1"
GROUP_ID="$2"
ARTIFACT_ID="$3"
ROOT_DIR = "$4"
VERSION="1.0-SNAPSHOT"

# 删除旧项目，如果存在的话
if [ -d "$PROJECT_DIR" ]; then
    rm -rf "$PROJECT_DIR"
fi
cd $ROOT_DIR
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
mkdir -p "$ROOT_DIR/$PROJECT_DIR/src/main/java/$GROUP_ID/$PROJECT_DIR/spider/service"
# 创建spider方法的入参，出参方法路径
mkdir -p "$ROOT_DIR/$PROJECT_DIR/src/main/java/$GROUP_ID/$PROJECT_DIR/spider/data"
# 创建 spider配置类路径
mkdir -p "$ROOT_DIR/$PROJECT_DIR/src/main/java/$GROUP_ID/$PROJECT_DIR/config"
# 创建spider的resource中的，配置文件路口
mkdir -p "$ROOT_DIR/$PROJECT_DIR/src/main/resources"