package cn.spider.framework.code.agent.function;

import cn.spider.framework.code.agent.util.FltlUtil;
import cn.spider.framework.code.agent.util.ShellUtil;
import com.google.common.base.Preconditions;
import freemarker.template.TemplateException;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AreaProjectNode {
    // 基于模板项目进行构建项目


    public void generateProject(String projectName, String groupId, String artifactId, String finalPath,String version,String javaFilePath) {
        String groupPath = groupId.replace('.', '/');
        String shellParam = projectName + " " + groupId + " " + artifactId + " " + version + " " + groupPath + " " + finalPath + " " + javaFilePath;
        ShellUtil.runShell("generate_project.sh", shellParam);
    }

    // 重新生成pom文件
    public void buildAreaPom(String outPath, String groupId, String artifactId, String version, String projectName, String projectDescription,String baseGroupId,String baseArtifactId,String baseVersion) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("groupId", groupId);
        dataModel.put("artifactId", artifactId);
        dataModel.put("version", version);
        dataModel.put("projectName", projectName);
        dataModel.put("projectDescription", projectDescription);
        dataModel.put("baseGroupId",baseGroupId);
        dataModel.put("baseArtifactId",baseArtifactId);
        dataModel.put("baseVersion",baseVersion);
        try {
            FltlUtil.generateFile(outPath, dataModel, "function_pom.ftl","pom.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    // 重新生成pom文件
    public void buildBasePom(String outPath, String groupId, String artifactId, String version) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("groupId", groupId);
        dataModel.put("artifactId", artifactId);
        dataModel.put("version", version);
        try {
            FltlUtil.generateFile(outPath, dataModel, "base_pom.ftl","pom.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    // 重新生成yml文件
    public void buildYml(String outPath, String artifactId) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("applicationName", artifactId);
        try {
            FltlUtil.generateFile(outPath, dataModel, "yml.ftl","application.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    // 构建启动类
    public void buildStart(String outPath, String className, String classPath,String mapperPath) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("startPath", classPath);
        dataModel.put("startClassName", className);
        dataModel.put("mapperPath",mapperPath);
        try {
            FltlUtil.generateFile(outPath, dataModel, "startClass.ftl",className+".java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    // 构建配置类
    public void buildConfig(String outPath, String classPath) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("configPath", classPath);
        try {
            FltlUtil.generateFile(outPath, dataModel, "config.ftl","AreaApplicationConfig.java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void buildServiceClass(String outPath, String serviceClass,String className) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("serviceCode", serviceClass);
        try {
            FltlUtil.generateFile(outPath, dataModel, "service.ftl",className + ".java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void buildParamClass(String outPath, String paramClass,String className) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("paramClass", paramClass);
        try {
            FltlUtil.generateFile(outPath, dataModel, "param.ftl",className + ".java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void buildParamResult(String outPath, String resultClass,String className) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("resultClass", resultClass);
        try {
            FltlUtil.generateFile(outPath, dataModel, "result.ftl",className + ".java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void mkdirPath(String path){
        ShellUtil.runShell("mkdir_area_base_path.sh", path);
    }


    // 进行mvn install
    public void mvnInstall(String directory) {
        ShellUtil.runShell("run_mvn_install.sh", directory);
    }

    // 获取到到.jar的包
    public Path readJarFilesInDirectory(String directoryPath) throws IOException {
        Path dirPath = Paths.get(directoryPath);

        // 列出目录中的所有文件
        List<Path> jarFiles = Files.walk(dirPath)
                .filter(p -> p.toString().endsWith(".jar"))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(jarFiles)) {
            Preconditions.checkArgument(false, "mvn-install后没有找到对应的jar文件，请检查");
        }
        return jarFiles.get(0);
    }

    // 上传oss-调用spider进行上传

    // 解析出

    // 部署到宿主机
    // 获取spider上面功能信息
    // 执行完成
}
