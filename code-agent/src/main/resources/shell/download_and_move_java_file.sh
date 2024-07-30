#!/bin/bash

# 检查参数数量
if [ "$#" -ne 3 ]; then
    echo "Usage: $0 <java_file_url> <target_dir> <class_name>"
    exit 1
fi

# 定义变量
JAVA_FILE_URL="$1"
TARGET_DIR="$2"
CLASS_NAME="$3"

# 检查wget是否可用
if ! command -v wget &> /dev/null; then
    echo "wget could not be found. Please install it."
    exit 1
fi

# 下载文件
wget -O $CLASS_NAME "$JAVA_FILE_URL"

# 检查文件是否下载成功
if [ $? -ne 0 ]; then
    echo "Failed to download the file."
    exit 1
fi

# 移动文件到目标目录
mv $CLASS_NAME "$TARGET_DIR"

# 检查移动操作是否成功
if [ $? -ne 0 ]; then
    echo "Failed to move the file to the target directory."
    exit 1
fi

echo "File downloaded and moved successfully."