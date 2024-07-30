package cn.spider.framework.code.agent.function;

import cn.spider.framework.code.agent.util.ClassUtil;

public class FunctionManager {

    // 项目的环境节点
    private String rootPath;

    private String defaultGroupId;

    private String defaultVersion;

    private AreaProjectNode areaProjectNode;

    private String directorySegmentation;

    private String startClassPath;


    public String buildProject(String serviceClass, String paramClass, String resultClass) {
        // 获取项目的名称 名称的命名为 area + functionName
        // 从 serviceClass中获取方法的名称
        String artifactId = ClassUtil.extractClassName(serviceClass);
        String projectDir = artifactId;
        // 构建项目
        areaProjectNode.generateProject(projectDir,this.defaultGroupId,artifactId,this.rootPath);
        // 覆盖pom
        // 获取pom文件的路径
        String pomPath = rootPath+this.directorySegmentation+projectDir+this.directorySegmentation;
        areaProjectNode.buildPom(pomPath,this.defaultGroupId,artifactId,this.defaultVersion,projectDir,"area_function");
        // 新增启动类
        String startClassPath = rootPath+this.directorySegmentation+projectDir+this.directorySegmentation+this.startClassPath+this.directorySegmentation+projectDir;
        areaProjectNode.buildStart(startClassPath,artifactId,this.startClassPath);
        // 现在配置类

        // 下载并移动功能文件

        // mvn install

        // 读取jar，上传到oss

        // 通过url进行部署到宿主机
        return null;
    }

    // 卸载功能

    // 替换功能
}
