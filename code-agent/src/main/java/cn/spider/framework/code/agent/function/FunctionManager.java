package cn.spider.framework.code.agent.function;

import cn.spider.framework.code.agent.spider.SpiderClient;
import cn.spider.framework.code.agent.util.ClassUtil;
import com.alipay.sofa.common.utils.StringUtil;

import java.io.IOException;
import java.nio.file.Path;

public class FunctionManager {

    // 项目的环境节点
    private String rootPath;

    private String defaultGroupId;

    private String defaultVersion;

    private AreaProjectNode areaProjectNode;

    // 其实就是"/"
    private String directorySegmentation;

    private String startClassPath;

    private SpiderClient spiderClient;


    public String buildProject(String serviceClass, String paramClass, String resultClass) {
        // 获取项目的名称 名称的命名为 area + functionName
        // 从 serviceClass中获取方法的名称
        try {
            String className = ClassUtil.extractClassName(serviceClass);
            String artifactId = className;
            String projectDir = artifactId;
            // 构建项目
            areaProjectNode.generateProject(projectDir,this.defaultGroupId,artifactId,this.rootPath);
            // 覆盖pom
            // 获取pom文件的路径
            String pomPath = rootPath+this.directorySegmentation+projectDir+this.directorySegmentation;
            areaProjectNode.buildPom(pomPath,this.defaultGroupId,artifactId,this.defaultVersion,projectDir,"area_function");
            // 新增启动类
            String projectPath = this.defaultGroupId.replace("\\.","/");
            String startClassPath = rootPath+this.directorySegmentation+projectDir+this.directorySegmentation+this.startClassPath+this.directorySegmentation+projectPath;
            areaProjectNode.buildStart(startClassPath,className,projectPath);
            // 现在配置类
            String configPath = startClassPath+this.directorySegmentation+ "config";
            String configPackage = projectPath+this.directorySegmentation+"config";
            areaProjectNode.buildConfig(configPath,configPackage);
            // 构建service信息
            String serviceClassPath = startClassPath+this.directorySegmentation+className+"/spider/service";
            areaProjectNode.buildParamClass(serviceClassPath,serviceClass);
            // 构建 param
            String paramClassPath = startClassPath+this.directorySegmentation+className+"/spider/data";
            if(StringUtil.isNotEmpty(paramClass)){
                areaProjectNode.buildParamClass(paramClassPath,paramClass);
            }
            if(StringUtil.isNotEmpty(resultClass)){
                areaProjectNode.buildParamResult(paramClassPath,resultClass);
            }
            String shellParam = pomPath;
            // mvn install
            areaProjectNode.invokeMavenBuild(shellParam);
            // 读取文件进行上传
            String jarPath = pomPath+this.directorySegmentation+"target";
            Path path = areaProjectNode.readJarFilesInDirectory(jarPath);
            String url = spiderClient.uploadFile(path);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 通过url进行部署到宿主机
        return null;
    }

    // 卸载功能

    // 替换功能
}
