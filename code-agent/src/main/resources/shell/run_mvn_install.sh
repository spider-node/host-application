#!/bin/bash


# 获取传递给脚本的目录路径
project_dir="$1"

# 转移到指定的项目目录
cd $project_dir

# 执行mvn install并捕获输出
mvn install > install.log 2>&1

# 显示执行结果
if [ $? -eq 0 ]; then
    echo "Maven install completed successfully."
else
    echo "Maven install failed. Check install.log for details."
fi