package cn.spider.framework.code.agent.project.factory;

import cn.spider.framework.code.agent.function.AreaProjectNode;
import cn.spider.framework.code.agent.project.factory.data.ProjectParam;
import cn.spider.framework.code.agent.project.path.ProjectPathService;
import cn.spider.framework.code.agent.project.path.data.ProjectPath;
import cn.spider.framework.code.agent.util.ClassUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ProjectFactory {
    @Value("${project.rootPath}")
    private String rootPath;

    @Value("${project.pom.defaultGroupId}")
    private String defaultGroupId;

    @Value("${project.pom.defaultVersion}")
    private String defaultVersion;

    @Resource
    private ProjectPathService projectPathService;

    @Resource
    private AreaProjectNode areaProjectNode;

    public void createBaseProject() {

    }

    public void createAreaProject(ProjectParam param) {
        String serviceClass = param.getServiceClass();
        String className = ClassUtil.extractClassName(serviceClass);
        ProjectPath projectPath = projectPathService.buildAreaProjectPath(param.getDatasource(),param.getTableName(),"",className);
        // 生成项目
        areaProjectNode.generateProject(projectPath.getArtifactId(),this.defaultGroupId,projectPath.getArtifactId(),projectPath.getProjectAreaPath(),projectPath.getVersion(),projectPath.getJavaFilePath());


    }
}
