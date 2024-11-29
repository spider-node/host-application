package cn.spider.framework.code.agent.function;

import cn.spider.framework.code.agent.util.FltlUtil;
import cn.spider.framework.code.agent.util.ShellUtil;
import com.google.common.base.Preconditions;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class AreaProjectNode {
    // 基于模板项目进行构建项目


    public Map<String, String> generateProject(String projectName, String groupId, String artifactId, String finalPath, String version, String javaFilePath) {
        String groupPath = groupId.replace('.', '/');
        String shellParam = projectName + " " + groupId + " " + artifactId + " " + version + " " + groupPath + " " + finalPath + " " + javaFilePath;
        return ShellUtil.runShell("generate_project.sh", shellParam);
    }

    // 重新生成pom文件
    public void buildAreaPom(String outPath, String groupId, String artifactId, String version, String projectName, String projectDescription, String baseGroupId, String baseArtifactId, String baseVersion,String mavenPom) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("groupId", groupId);
        dataModel.put("artifactId", artifactId);
        dataModel.put("version", version);
        dataModel.put("projectName", projectName);
        dataModel.put("projectDescription", projectDescription);
        dataModel.put("baseGroupId", baseGroupId);
        dataModel.put("baseArtifactId", baseArtifactId);
        dataModel.put("baseVersion", baseVersion);
        dataModel.put("mavenPom", mavenPom);
        try {
            FltlUtil.generateFile(outPath, dataModel, "function_pom.ftl", "pom.xml");
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
            FltlUtil.generateFile(outPath, dataModel, "base_pom.ftl", "pom.xml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    // 重新生成yml文件
    public void buildYml(String outPath, String artifactId, String version, String datasourceName, String areaName, String areaId, Integer taskId) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("applicationName", artifactId);
        dataModel.put("artifactId", artifactId);
        dataModel.put("version", version);
        dataModel.put("datasourceName", datasourceName);
        dataModel.put("areaId", areaId);
        dataModel.put("areaName", areaName);
        dataModel.put("taskId", taskId + "");
        try {
            FltlUtil.generateFile(outPath, dataModel, "application.properties.ftl", "application.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    // 构建启动类
    public void buildStart(String outPath, String className, String classPath, String mapperPath) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("startPath", classPath);
        dataModel.put("startClassName", className);
        dataModel.put("mapperPath", mapperPath);
        try {
            FltlUtil.generateFile(outPath, dataModel, "startClass.ftl", className + ".java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    // 构建配置类
    public void buildConfig(String outPath, String classPath, String baseScanPath, String bizScanPath) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("configPath", classPath);
        dataModel.put("baseScanPackage", baseScanPath);
        dataModel.put("bizScanPackage", baseScanPath);
        try {
            FltlUtil.generateFile(outPath, dataModel, "config.ftl", "AreaApplicationConfig.java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void buildServiceClass(String outPath, String serviceClass, String className) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("serviceCode", serviceClass);
        try {
            FltlUtil.generateFile(outPath, dataModel, "service.ftl", className + ".java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void buildParamClass(String outPath, String paramClass, String className) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("paramClass", paramClass);
        try {
            FltlUtil.generateFile(outPath, dataModel, "param.ftl", className + ".java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void buildParamResult(String outPath, String resultClass, String className) {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("resultClass", resultClass);
        try {
            FltlUtil.generateFile(outPath, dataModel, "result.ftl", className + ".java");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    public void mkdirPath(String path) {
        ShellUtil.runShell("mkdir_area_base_path.sh", path);
    }


    // 进行mvn install
    public Map<String, String> mvnInstall(String directory) throws IOException {
        Map<String, String> mvnInstallRunResult = ShellUtil.runShell("run_mvn_install.sh", directory);
        if (mvnInstallRunResult.get("code").equals("200")) {
            return mvnInstallRunResult;
        }
        // 读取 mvn install后的文件，提示已经mvn install失败
        String mvnInstallError = readMvnInstall(directory);
        log.error("mvn install fail {}", mvnInstallError);
        // 把异常信息添加进去
        mvnInstallRunResult.put("mvnInstallError", mvnInstallError);
        return mvnInstallRunResult;
    }

    // 获取到到.jar的包
    public Path readJarFilesInDirectory(String directoryPath) throws IOException {
        Path dirPath = Paths.get(directoryPath);
        log.info("加载文件的路径 {}", directoryPath);
        // 列出目录中的所有文件
        List<Path> jarFiles = Files.walk(dirPath)
                .filter(p -> p.toString().endsWith("-ark-biz.jar"))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(jarFiles)) {
            Preconditions.checkArgument(false, "mvn-install后没有找到对应的jar文件，请检查");
        }
        return jarFiles.get(0);
    }

    // 读取 install.log 信息

    public String readMvnInstall(String directoryPath) throws IOException {
        Path dirPath = Paths.get(directoryPath);

        // 列出目录中的所有文件
        List<Path> installLogs = Files.walk(dirPath)
                .filter(p -> p.toString().endsWith(".log"))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(installLogs)) {
            Preconditions.checkArgument(false, "mvn-install后没有找到  install.log");
        }
        Path installLog = installLogs.get(0);
        return Files.readAllLines(installLog, StandardCharsets.UTF_8).stream()
                .collect(Collectors.joining(System.lineSeparator()));
    }


    // 上传oss-调用spider进行上传

    // 解析出

    // 部署到宿主机
    // 获取spider上面功能信息
    // 执行完成
}
