package cn.spider.framework.code.agent.project.path;

import cn.spider.framework.code.agent.project.path.data.ProjectPath;
import cn.spider.framework.code.agent.util.ClassUtil;
import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ProjectPathService {
    // 根据datasource+table 得出项目路径, 通过功能
    @Value("${project.rootPath}")
    private String rootPath;

    @Value("${project.pom.defaultGroupId}")
    private String defaultGroupId;

    @Value("${project.pom.defaultVersion}")
    private String defaultVersion;

    private final String directorySegmentation = "/";

    private final String startClassPath = "src/main/java";


    public ProjectPath buildAreaProjectPath(String database, String table, String version, String className) {
        version = StringUtils.isEmpty(version) ? defaultVersion : version;
        String projectRootPath = this.rootPath + this.directorySegmentation + database + this.directorySegmentation + table + this.directorySegmentation;
        String artifactId = table + "_" + ClassUtil.camelToSnake(className);
        String projectFinalPath = projectRootPath + className + this.directorySegmentation + version + this.directorySegmentation;
        String pomPath = projectFinalPath +artifactId + this.directorySegmentation;
        String groupIdPath = this.defaultGroupId.replace(".", "/");
        String artifactIdPath = ClassUtil.replaceUnderscoresWithSlashes(artifactId);
        String mainPath = pomPath + this.startClassPath + this.directorySegmentation + groupIdPath + this.directorySegmentation + artifactIdPath;
        String servicePath = mainPath + this.directorySegmentation + "spider/service";
        String dataPath = mainPath + this.directorySegmentation + "spider/data";
        String configPath = mainPath + this.directorySegmentation + "config";
        String propertiesPath = pomPath + "src/main/resources";
        String startClassPackagePath = defaultGroupId + "." + artifactId.replace("_",".");
        String configPackage = startClassPackagePath + ".config";
        return ProjectPath.builder()
                .projectAreaPath(projectFinalPath)
                .projectRootPath(projectRootPath)
                .artifactId(artifactId)
                .version(version)
                .javaFilePath(artifactIdPath)
                .pomPath(pomPath)
                .configPath(configPath)
                .servicePath(servicePath)
                .propertiesPath(propertiesPath)
                .dataPath(dataPath)
                .startClassPackagePath(startClassPackagePath)
                .configPackage(configPackage)
                .mainPath(mainPath)
                .build();
    }

    public ProjectPath buildBaseProjectPath(String database, String table, String version) {
        version = StringUtils.isEmpty(version) ? defaultVersion : version;
        String projectRootPath = this.rootPath + this.directorySegmentation + database + this.directorySegmentation + table + this.directorySegmentation;
        String projectFinalPath = projectRootPath + "base" + this.directorySegmentation + version + this.directorySegmentation;
        String artifactId = database + "_" + table + "_base";
        String pomPath = projectFinalPath + artifactId;
        // 代码可以移动到这个目录下
        String codePath = pomPath + this.directorySegmentation + this.startClassPath + this.directorySegmentation;
        String rootPackagePath = this.defaultGroupId +  "." + artifactId.replace("_",".");
        String groupIdPath = this.defaultGroupId.replace(".", "/");
        String artifactIdPath = ClassUtil.replaceUnderscoresWithSlashes(artifactId);
        String mainPath = pomPath + this.directorySegmentation + this.startClassPath + this.directorySegmentation + groupIdPath + this.directorySegmentation + artifactIdPath;
        String entityPath = mainPath + this.directorySegmentation + "entity";
        return ProjectPath.builder()
                .projectAreaPath(projectFinalPath)
                .projectRootPath(projectRootPath)
                .artifactId(artifactId)
                .groupId(this.defaultGroupId)
                .version(version)
                .codePath(codePath)
                .rootPackagePath(rootPackagePath)
                .pomPath(pomPath)
                .entityPath(entityPath)
                .build();
    }


}
