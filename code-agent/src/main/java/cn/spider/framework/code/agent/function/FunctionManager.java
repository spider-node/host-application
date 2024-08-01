package cn.spider.framework.code.agent.function;

import cn.spider.framework.code.agent.data.DeployAreaFunctionParam;
import cn.spider.framework.code.agent.spider.SpiderClient;
import cn.spider.framework.code.agent.util.ClassUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;

@Component
public class FunctionManager {

    // 项目的环境节点
    @Value("${project.rootPath}")
    private String rootPath;

    @Value("${project.pom.defaultGroupId}")
    private String defaultGroupId;

    @Value("${project.pom.defaultVersion}")
    private String defaultVersion;

    @Resource
    private AreaProjectNode areaProjectNode;

    // 其实就是"/"
    private final String directorySegmentation = "/";

    private final String startClassPath = "src/main/java";

    @Resource
    private SpiderClient spiderClient;


    public String buildProject(DeployAreaFunctionParam param) {
        String serviceClass = param.getServiceClass();
        String paramClass = param.getParamClass();
        String resultClass = param.getResultClass();
        // 获取项目的名称 名称的命名为 area + functionName
        // 从 serviceClass中获取方法的名称
        try {
            String className = ClassUtil.extractClassName(serviceClass);
            String artifactId = className;
            String projectDir = artifactId;
            // 构建项目
            //String spiderAreaFunctionPath = rootPath+this.directorySegmentation+projectDir+this.directorySegmentation;
            areaProjectNode.generateProject(projectDir,this.defaultGroupId,artifactId,this.rootPath,param.getAreaName());
            // 覆盖pom
            // 获取pom文件的路径
            /*String pomPath = rootPath+this.directorySegmentation+projectDir+this.directorySegmentation;
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
            if(!StringUtils.isEmpty(paramClass)){
                areaProjectNode.buildParamClass(paramClassPath,paramClass);
            }
            if(!StringUtils.isEmpty(resultClass)){
                areaProjectNode.buildParamResult(paramClassPath,resultClass);
            }
            String shellParam = pomPath;
            // mvn install
            areaProjectNode.invokeMavenBuild(shellParam);
            // 读取文件进行上传
            String jarPath = pomPath+this.directorySegmentation+"target";
            Path path = areaProjectNode.readJarFilesInDirectory(jarPath);
            String url = spiderClient.uploadFile(path);*/

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 通过url进行部署到宿主机
        return null;
    }

    // 卸载功能

    // 替换功能
}
