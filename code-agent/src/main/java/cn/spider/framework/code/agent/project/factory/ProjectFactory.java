package cn.spider.framework.code.agent.project.factory;

import cn.hutool.core.lang.Assert;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.entity.AreaDatasourceInfo;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.service.AreaDatasourceInfoService;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitParam;
import cn.spider.framework.code.agent.areabase.utils.CodeGenerator3;
import cn.spider.framework.code.agent.function.AreaProjectNode;
import cn.spider.framework.code.agent.project.factory.data.ProjectParam;
import cn.spider.framework.code.agent.project.path.ProjectPathService;
import cn.spider.framework.code.agent.project.path.data.ProjectPath;
import cn.spider.framework.code.agent.util.ClassUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
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

    @Resource
    private AreaDatasourceInfoService datasourceInfoService;

    public void createBaseProject(AreaDomainInitParam param) {
        // 获取各种路径
        AreaDatasourceInfo areaDatasourceInfo = datasourceInfoService.getBaseMapper().selectById(param.getDatasourceId());
        Assert.notNull(areaDatasourceInfo,"当前数据源信息不存在，请核实后在请求");
        ProjectPath projectPath = projectPathService.buildBaseProjectPath(areaDatasourceInfo.getName(),param.getTableName(),"");
        log.info("createAreaProject {}", JSON.toJSONString(projectPath));
        // 通过shell脚本进行，项目根路径
        areaProjectNode.mkdirPath(projectPath.getCodePath());
        param.setPackageName(projectPath.getRootPackagePath());
        CodeGenerator3.initCode(areaDatasourceInfo,param,projectPath.getCodePath());
        // 构建pom文件
        areaProjectNode.buildBasePom(projectPath.getPomPath(),projectPath.getGroupId(),projectPath.getArtifactId(),projectPath.getVersion());
        // mvn install
        areaProjectNode.mvnInstall(projectPath.getPomPath());
    }

    public void createAreaProject(ProjectParam param) {
        String serviceClass = param.getServiceClass();
        String className = ClassUtil.extractClassName(serviceClass);
        ProjectPath projectPath = projectPathService.buildAreaProjectPath(param.getDatasource(),param.getTableName(),"",className);
        log.info("createAreaProject {}", JSON.toJSONString(projectPath));
        // 生成项目
        areaProjectNode.generateProject(projectPath.getArtifactId(),this.defaultGroupId,projectPath.getArtifactId(),projectPath.getProjectAreaPath(),projectPath.getVersion(),projectPath.getJavaFilePath());
        // 生成java启动类
        areaProjectNode.buildStart(projectPath.getMainPath(),className + "Start",projectPath.getStartClassPackagePath());
        // 生成java 业务类
        areaProjectNode.buildParamClass(projectPath.getServicePath(),serviceClass,className);
        // 现在配置类
        areaProjectNode.buildConfig(projectPath.getConfigPath(),projectPath.getConfigPackage());
        // 更新pom文件
        areaProjectNode.buildAreaPom(projectPath.getPomPath(),this.defaultGroupId,projectPath.getArtifactId(),projectPath.getVersion(),className,"");
        // 新增yml配置
        areaProjectNode.buildYml(projectPath.getPropertiesPath(),projectPath.getArtifactId());

        // 生成参数类
        if(!StringUtils.isEmpty(param.getParamClass())){
            String paramClassName =ClassUtil.extractClassName(param.getParamClass());
            areaProjectNode.buildParamClass(projectPath.getDataPath(),param.getParamClass(),paramClassName);
        }
        if(!StringUtils.isEmpty(param.getResultClass())){
            String resultClassName =ClassUtil.extractClassName(param.getResultClass());
            areaProjectNode.buildParamResult(projectPath.getDataPath(),param.getResultClass(),resultClassName);
        }
        areaProjectNode.mvnInstall(projectPath.getPomPath());
    }
}
