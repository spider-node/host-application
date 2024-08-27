package cn.spider.framework.code.agent.project.factory;

import cn.hutool.core.lang.Assert;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.entity.AreaDatasourceInfo;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.service.AreaDatasourceInfoService;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInfo;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitParam;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.service.AreaDomainInfoService;
import cn.spider.framework.code.agent.areabase.modules.function.entity.AreaDomainFunctionInfo;
import cn.spider.framework.code.agent.areabase.modules.function.entity.enums.AreaFunctionStatus;
import cn.spider.framework.code.agent.areabase.modules.function.service.IAreaDomainFunctionInfoService;
import cn.spider.framework.code.agent.areabase.modules.sonarea.entity.SpiderSonArea;
import cn.spider.framework.code.agent.areabase.modules.sonarea.service.ISpiderSonAreaService;
import cn.spider.framework.code.agent.areabase.utils.CodeGenerator3;
import cn.spider.framework.code.agent.function.AreaProjectNode;
import cn.spider.framework.code.agent.project.factory.data.CreateProjectResult;
import cn.spider.framework.code.agent.project.factory.data.InitAreaBaseResult;
import cn.spider.framework.code.agent.project.factory.data.ProjectParam;
import cn.spider.framework.code.agent.project.factory.data.enums.CreateProjectStatus;
import cn.spider.framework.code.agent.project.path.ProjectPathService;
import cn.spider.framework.code.agent.project.path.data.ProjectPath;
import cn.spider.framework.code.agent.spider.SpiderClient;
import cn.spider.framework.code.agent.spider.data.DeployPluginParam;
import cn.spider.framework.code.agent.spider.data.DeployPluginResult;
import cn.spider.framework.code.agent.util.ClassUtil;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@Component
public class ProjectFactory {

    @Value("${project.pom.defaultGroupId}")
    private String defaultGroupId;

    @Resource
    private ProjectPathService projectPathService;

    @Resource
    private AreaProjectNode areaProjectNode;

    @Resource
    private AreaDatasourceInfoService datasourceInfoService;

    @Resource
    private AreaDomainInfoService areaDomainInfoService;

    @Resource
    private IAreaDomainFunctionInfoService areaDomainFunctionInfoService;

    @Resource
    private ISpiderSonAreaService spiderSonAreaService;

    @Resource
    private SpiderClient spiderClient;

    /**
     *
     * @param param 创建 子域信息与生成base内容
     * @return
     */
    public InitAreaBaseResult initBaseProject(AreaDomainInitParam param) {
        InitAreaBaseResult initAreaBaseResult = new InitAreaBaseResult();
        SpiderSonArea sonArea = spiderSonAreaService.lambdaQuery()
                .eq(SpiderSonArea::getAreaName, param.getAreaName())
                .eq(SpiderSonArea::getSonAreaName, param.getSonAreaName()).one();
        if (Objects.isNull(sonArea)) {
            sonArea = new SpiderSonArea();
            sonArea.setAreaId(param.getAreaId());
            sonArea.setAreaName(param.getAreaName());
            sonArea.setSonAreaName(param.getSonAreaName());
            sonArea.setTableName(param.getTableName());
            sonArea.setDatasource(param.getDatasource());
            spiderSonAreaService.save(sonArea);
            initAreaBaseResult.setSonArea(sonArea);
        }
        param.setTableName(sonArea.getTableName());
        // 获取各种路径
        AreaDatasourceInfo areaDatasourceInfo = datasourceInfoService.lambdaQuery().eq(AreaDatasourceInfo::getDatasource, sonArea.getDatasource()).one();
        Assert.notNull(areaDatasourceInfo, "当前数据源信息不存在，请核实后在请求");
        ProjectPath projectPath = projectPathService.buildBaseProjectPath(areaDatasourceInfo.getDatasource(), sonArea.getTableName(), "");
        AreaDomainInfo areaDomain = areaDomainInfoService.lambdaQuery()
                .eq(AreaDomainInfo::getDatasourceId, areaDatasourceInfo.getId())
                .eq(AreaDomainInfo::getTableName, sonArea.getTableName())
                .eq(AreaDomainInfo::getVersion, projectPath.getVersion())
                .last(" order by create_time desc limit 1").one();

        if (Objects.nonNull(areaDomain)) {
            String finalVersion = ClassUtil.incrementVersion(areaDomain.getVersion());
            projectPath = projectPathService.buildBaseProjectPath(areaDatasourceInfo.getDatasource(), param.getTableName(), finalVersion);
        }
        log.info("createAreaProject {}", JSON.toJSONString(projectPath));
        // 通过shell脚本进行，项目根路径
        areaProjectNode.mkdirPath(projectPath.getCodePath());
        param.setPackageName(projectPath.getRootPackagePath());
        CodeGenerator3.initCode(areaDatasourceInfo, param, projectPath.getCodePath());
        // 构建pom文件
        areaProjectNode.buildBasePom(projectPath.getPomPath(), projectPath.getGroupId(), projectPath.getArtifactId(), projectPath.getVersion());
        // mvn install
        areaProjectNode.mvnInstall(projectPath.getPomPath());
        // 更新信息到领域中
        AreaDomainInfo areaDomainInfo = CodeGenerator3.initAreaDomainInfo(areaDatasourceInfo, param, projectPath.getCodePath());
        areaDomainInfo.setVersion(projectPath.getVersion());
        areaDomainInfo.setArtifactId(projectPath.getArtifactId());
        areaDomainInfo.setGroupId(projectPath.getGroupId());
        areaDomainInfo.setDatasourceName(areaDatasourceInfo.getDatasource());
        areaDomainInfo.setDatasourceId(areaDatasourceInfo.getId());
        areaDomainInfo.setAreaId(sonArea.getAreaId());
        areaDomainInfo.setAreaName(sonArea.getAreaName());
        areaDomainInfoService.save(areaDomainInfo);
        initAreaBaseResult.setAreaDomainInfo(areaDomainInfo);
        // 调用spider-加载-areaDomainInfo
        return initAreaBaseResult;
    }

    /**
     * 相同项目会进行覆盖
     *
     * @param param
     */
    public CreateProjectResult createAreaProject(ProjectParam param) {
        // 使用最新的基础版本
        AreaDomainInfo areaDomain = areaDomainInfoService.lambdaQuery()
                .eq(AreaDomainInfo::getDatasourceName, param.getDatasource())
                .eq(AreaDomainInfo::getTableName, param.getTableName())
                .last(" order by create_time desc limit 1").one();

        Preconditions.checkArgument(Objects.nonNull(areaDomain), "没有领域基础信息,请初始化领域基础信息");
        String serviceClass = param.getServiceClass();
        String className = ClassUtil.extractClassName(serviceClass);
        String mapperPath = areaDomain.getDomainObjectServicePackage().replaceAll("service$", "mapper").replaceAll("^package\\s+", "");
        ProjectPath projectPath = projectPathService.buildAreaProjectPath(param.getDatasource(), param.getTableName(), "", className);
        AreaDomainFunctionInfo areaDomainFunctionInfo = areaDomainFunctionInfoService.lambdaQuery()
                .eq(AreaDomainFunctionInfo::getDatasourceName, param.getDatasource())
                .eq(AreaDomainFunctionInfo::getFunctionName, className)
                .eq(AreaDomainFunctionInfo::getVersion, projectPath.getVersion())
                .last("order by create_time limit 1").one();
        // 存在就升级版本
        if (Objects.nonNull(areaDomainFunctionInfo)) {
            String finalVersion = ClassUtil.incrementVersion(projectPath.getVersion());
            areaDomainFunctionInfo.setVersion(finalVersion);
            projectPath = projectPathService.buildAreaProjectPath(param.getDatasource(), param.getTableName(), finalVersion, className);
        }
        AreaDomainFunctionInfo areaDomainFunctionInfoNew = new AreaDomainFunctionInfo();
        areaDomainFunctionInfoNew.setAreaFunctionClass(param.getServiceClass());
        areaDomainFunctionInfoNew.setAreaFunctionParamClass(param.getParamClass());
        areaDomainFunctionInfoNew.setAreaFunctionResultClass(param.getResultClass());
        areaDomainFunctionInfoNew.setDatasourceName(param.getDatasource());
        areaDomainFunctionInfoNew.setTableName(param.getTableName());
        areaDomainFunctionInfoNew.setFunctionName(className);
        areaDomainFunctionInfoNew.setFunctionDesc(param.getDesc());
        areaDomainFunctionInfoNew.setVersion(projectPath.getVersion());
        areaDomainFunctionInfoNew.setStatus(AreaFunctionStatus.INIT);
        areaDomainFunctionInfoNew.setGroupId(projectPath.getGroupId());
        areaDomainFunctionInfoNew.setDatasourceId(areaDomain.getDatasourceId());
        areaDomainFunctionInfoNew.setArtifactId(projectPath.getArtifactId());
        areaDomainFunctionInfoNew.setBaseVersion(areaDomain.getVersion());
        CreateProjectResult projectResult = new CreateProjectResult();
        try {
            log.info("createAreaProject {}", JSON.toJSONString(projectPath));
            // 生成项目
            areaProjectNode.generateProject(projectPath.getArtifactId(), this.defaultGroupId, projectPath.getArtifactId(), projectPath.getProjectAreaPath(), projectPath.getVersion(), projectPath.getJavaFilePath());
            // 生成java启动类
            areaProjectNode.buildStart(projectPath.getMainPath(), className + "Start", projectPath.getStartClassPackagePath(), mapperPath);
            // 生成java 业务类
            areaProjectNode.buildParamClass(projectPath.getServicePath(), serviceClass, className);
            // 现在配置类- 配置 扫描base的路径,
            areaProjectNode.buildConfig(projectPath.getConfigPath(), projectPath.getConfigPackage(), areaDomain.getDomainObjectServicePackage(), projectPath.getStartClassPackagePath());
            // 更新pom文件
            areaProjectNode.buildAreaPom(projectPath.getPomPath(), this.defaultGroupId, projectPath.getArtifactId(), projectPath.getVersion(), className, "", areaDomain.getGroupId(), areaDomain.getArtifactId(), areaDomain.getVersion());
            // 新增yml配置
            areaProjectNode.buildYml(projectPath.getPropertiesPath(), projectPath.getArtifactId(), projectPath.getVersion(), param.getDatasource(), areaDomain.getAreaName(), areaDomain.getAreaId());
            // 生成参数类
            if (!StringUtils.isEmpty(param.getParamClass())) {
                String paramClassName = ClassUtil.extractClassName(param.getParamClass());
                areaProjectNode.buildParamClass(projectPath.getDataPath(), param.getParamClass(), paramClassName);
            }
            if (!StringUtils.isEmpty(param.getResultClass())) {
                String resultClassName = ClassUtil.extractClassName(param.getResultClass());
                areaProjectNode.buildParamResult(projectPath.getDataPath(), param.getResultClass(), resultClassName);
            }
            areaProjectNode.mvnInstall(projectPath.getPomPath());

            // 读取文件目录下的jar包
            Path jar = areaProjectNode.readJarFilesInDirectory(projectPath.getJarFilePath());
            String url = spiderClient.uploadFile(jar);
            DeployPluginParam deployPluginParam = new DeployPluginParam();
            deployPluginParam.setBizVersion(projectPath.getVersion());
            deployPluginParam.setBizName(projectPath.getArtifactId());
            deployPluginParam.setBizUrl(url);
            DeployPluginResult deployPluginResult = spiderClient.deployPluginToApplication(deployPluginParam);
            projectResult.setStatus(deployPluginResult.getCode().equals("CMD_PROCESS_INTERNAL_ERROR") ? CreateProjectStatus.DEPLOY_FAIL : CreateProjectStatus.SUSS);
            projectResult.setErrorStackTrace(deployPluginResult.getErrorStackTrace());
            // 进行部署到宿主应用中
            areaDomainFunctionInfoNew.setStatus(AreaFunctionStatus.INIT_SUSS);
        } catch (Exception e) {
            areaDomainFunctionInfoNew.setStatus(AreaFunctionStatus.INIT_FAIL);
            log.error("init_area_function_fail {}", e.getMessage());
            projectResult.setStatus(CreateProjectStatus.INIT_FAIL);
            projectResult.setErrorStackTrace(e.getMessage());
        }
        areaDomainFunctionInfoService.save(areaDomainFunctionInfoNew);
        projectResult.setPluginInfo(areaDomainFunctionInfoNew);
        // 调用spider-加载数据
        return projectResult;
    }
}
