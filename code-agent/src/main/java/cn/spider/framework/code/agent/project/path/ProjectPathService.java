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


    public ProjectPath buildAreaProjectPath(String database, String table, String version, String className) {
        version = StringUtils.isEmpty(version) ? defaultVersion : version;
        String projectRootPath = this.rootPath + this.directorySegmentation + database + this.directorySegmentation + table + this.directorySegmentation;
        String projectFinalPath = projectRootPath +className + this.directorySegmentation + version + this.directorySegmentation;
        String artifactId = table + "_"+ ClassUtil.camelToSnake(className);

        return ProjectPath.builder()
                .projectAreaPath(projectFinalPath)
                .projectRootPath(projectRootPath)
                .artifactId(artifactId)
                .version(version)
                .javaFilePath(ClassUtil.replaceUnderscoresWithSlashes(artifactId))
                .build();
    }

    public ProjectPath buildBaseProjectPath(String database, String table, String version) {
        version = StringUtils.isEmpty(version) ? defaultVersion : version;
        String projectRootPath = this.rootPath + this.directorySegmentation + database + this.directorySegmentation + table + this.directorySegmentation;
        String projectFinalPath = projectRootPath +"base" + this.directorySegmentation + version + this.directorySegmentation;
        return ProjectPath.builder()
                .projectAreaPath(projectFinalPath)
                .projectRootPath(projectRootPath)
                .artifactId(table)
                .version(version)
                .build();
    }





}
