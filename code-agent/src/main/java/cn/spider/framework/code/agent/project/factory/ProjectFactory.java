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
import cn.spider.framework.code.agent.areabase.utils.ExceptionMessage;
import cn.spider.framework.code.agent.data.SonDomainModel;
import cn.spider.framework.code.agent.data.SonDomainModelInfo;
import cn.spider.framework.code.agent.data.SonDomainPomModel;
import cn.spider.framework.code.agent.function.AreaProjectNode;
import cn.spider.framework.code.agent.project.factory.data.InitAreaBaseResult;
import cn.spider.framework.code.agent.project.factory.data.ProjectParam;
import cn.spider.framework.code.agent.project.factory.data.enums.CreateProjectStatus;
import cn.spider.framework.code.agent.project.path.ProjectPathService;
import cn.spider.framework.code.agent.project.path.data.ProjectPath;
import cn.spider.framework.code.agent.spider.SpiderClient;
import cn.spider.framework.code.agent.util.ClassUtil;
import cn.spider.node.framework.code.agent.sdk.data.CreateProjectResult;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
     * @param param 创建 子域信息与生成base内容
     * @return
     */
    public InitAreaBaseResult initBaseProject(AreaDomainInitParam param) {
        InitAreaBaseResult initAreaBaseResult = new InitAreaBaseResult();
        SpiderSonArea sonArea = spiderSonAreaService.getById(param.getSonDomainId());
        initAreaBaseResult.setSonArea(sonArea);
        param.setTableName(sonArea.getTableName());
        // 获取各种路径
        AreaDatasourceInfo areaDatasourceInfo = datasourceInfoService.lambdaQuery().eq(AreaDatasourceInfo::getDatasource, sonArea.getDatasource()).one();
        Assert.notNull(areaDatasourceInfo, "当前数据源信息不存在，请核实后在请求");
        AreaDomainInfo areaDomain = areaDomainInfoService.lambdaQuery()
                .eq(AreaDomainInfo::getDatasourceId, areaDatasourceInfo.getId())
                .eq(AreaDomainInfo::getTableName, sonArea.getTableName())
                .last(" order by create_time desc limit 1").one();

        String finalVersion = Objects.nonNull(areaDomain) ? ClassUtil.getNextVersion(areaDomain.getVersion()) : "";
        ProjectPath projectPath = projectPathService.buildBaseProjectPath(areaDatasourceInfo.getDatasource(), param.getTableName(), finalVersion);

        log.info("createAreaProject {}", JSON.toJSONString(projectPath));
        try {
            // 通过shell脚本进行，项目根路径
            areaProjectNode.mkdirPath(projectPath.getCodePath());
            param.setPackageName(projectPath.getRootPackagePath());
            CodeGenerator3.initCode(areaDatasourceInfo, param, projectPath.getCodePath());
            // 构建pom文件
            areaProjectNode.buildBasePom(projectPath.getPomPath(), projectPath.getGroupId(), projectPath.getArtifactId(), projectPath.getVersion());
            // mvn install
            Map<String, String> mvnInstallInfo = areaProjectNode.mvnInstall(projectPath.getPomPath());
            if (!mvnInstallInfo.get("code").equals("200")) {
                initAreaBaseResult.setInitFail(mvnInstallInfo.get("mvnInstallError"));
                initAreaBaseResult.setInitCode("400");
                return initAreaBaseResult;
            }
            // 更新信息到领域中
            AreaDomainInfo areaDomainInfo = CodeGenerator3.initAreaDomainInfo(areaDatasourceInfo, param, projectPath.getCodePath());
            areaDomainInfo.setVersion(projectPath.getVersion());
            areaDomainInfo.setArtifactId(projectPath.getArtifactId());
            areaDomainInfo.setGroupId(projectPath.getGroupId());
            areaDomainInfo.setDatasourceName(areaDatasourceInfo.getDatasource());
            areaDomainInfo.setDatasourceId(areaDatasourceInfo.getId());
            areaDomainInfo.setAreaId(sonArea.getAreaId());
            areaDomainInfo.setAreaName(sonArea.getAreaName());
            areaDomainInfo.setSonAreaName(sonArea.getSonAreaName());
            areaDomainInfo.setSonAreaId(sonArea.getId());
            areaDomainInfoService.save(areaDomainInfo);
            initAreaBaseResult.setAreaDomainInfo(areaDomainInfo);
            initAreaBaseResult.setInitCode("200");
        } catch (IOException e) {
            initAreaBaseResult.setInitFail(ExceptionMessage.getStackTrace(e));
            initAreaBaseResult.setInitCode("400");
        }
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
        List<AreaDomainInfo> areaDomains = areaDomainInfoService.lambdaQuery()
                .in(AreaDomainInfo::getId, param.getBaseInfoId())
                .list();
        List<SonDomainModel> sonDomainModels = areaDomains.stream().map(item->{
            SonDomainModel sonDomainModel = new SonDomainModel();
            sonDomainModel.setSonAreaId(item.getSonAreaId());
            sonDomainModel.setSonAreaName(item.getSonAreaName());
            sonDomainModel.setBaseVersion(item.getVersion());
            return sonDomainModel;
        }).collect(Collectors.toList());

        List<SonDomainPomModel> sonDomainPomModels =  areaDomains.stream().map(item->{
            SonDomainPomModel pomModel = new SonDomainPomModel();
            pomModel.setGroupId(item.getGroupId());
            pomModel.setArtifactId(item.getArtifactId());
            pomModel.setVersion(item.getVersion());
            return pomModel;
        }).collect(Collectors.toList());
        AreaDomainInfo areaDomain = areaDomains.get(0);
        Preconditions.checkArgument(CollectionUtils.isNotEmpty(areaDomains), "没有领域基础信息,请初始化领域基础信息");
        String serviceClass = param.getServiceClass();
        String className = ClassUtil.extractClassName(serviceClass);
        Set<String> mapperPaths = areaDomains.stream().map(item -> item.getDomainObjectServicePackage().replaceAll("service$", "mapper").replaceAll("^package\\s+", "")).collect(Collectors.toSet());
        Set<String> servicePaths = areaDomains.stream().map(item-> item.getDomainObjectServicePackage()).collect(Collectors.toSet());
        // 版本号由外部指定
        ProjectPath projectPath = projectPathService.buildAreaProjectPath(param.getDatasource(), param.getTableName(), param.getVersion(), className);
        AreaDomainFunctionInfo areaDomainFunctionInfo = areaDomainFunctionInfoService.lambdaQuery()
                .eq(AreaDomainFunctionInfo :: getDomainFunctionVersionId, param.getDomainFunctionVersionId())
                .last("order by create_time desc limit 1").one();

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
        //areaDomainFunctionInfoNew.setBaseVersion(areaDomain.getVersion());
        areaDomainFunctionInfoNew.setAreaId(areaDomain.getAreaId());
        areaDomainFunctionInfoNew.setAreaName(areaDomain.getAreaName());
        SonDomainModelInfo sonDomainModelInfo = new SonDomainModelInfo();
        sonDomainModelInfo.setSonDomainModels(sonDomainModels);
        areaDomainFunctionInfoNew.setSonDomainModelInfo(sonDomainModelInfo);
        //areaDomainFunctionInfoNew.setSonAreaId(areaDomain.getId());
        //areaDomainFunctionInfoNew.setSonAreaName(areaDomain.getSonAreaName());
        areaDomainFunctionInfoNew.setTaskComponent(param.getTaskComponent());
        areaDomainFunctionInfoNew.setTaskId(param.getTaskId());
        areaDomainFunctionInfoNew.setTaskService(param.getTaskService());
        areaDomainFunctionInfoNew.setDomainFunctionVersionId(param.getDomainFunctionVersionId());
        areaDomainFunctionInfoNew.setId(Objects.nonNull(areaDomainFunctionInfo) ? areaDomainFunctionInfo.getId() : null);
        CreateProjectResult projectResult = new CreateProjectResult();
        try {
            log.info("createAreaProject {}", JSON.toJSONString(projectPath));
            // 生成项目
            areaProjectNode.generateProject(projectPath.getArtifactId(), this.defaultGroupId, projectPath.getArtifactId(), projectPath.getProjectAreaPath(), projectPath.getVersion(), projectPath.getJavaFilePath());
            // 生成java启动类
            areaProjectNode.buildStart(projectPath.getMainPath(), className + "Start", projectPath.getStartClassPackagePath(), mapperPaths);
            // 生成java 业务类
            areaProjectNode.buildParamClass(projectPath.getServicePath(), serviceClass, className);
            // 现在配置类- 配置 扫描base的路径,
            areaProjectNode.buildConfig(projectPath.getConfigPath(), projectPath.getConfigPackage(), servicePaths);
            // 更新pom文件
            areaProjectNode.buildAreaPom(projectPath.getPomPath(), this.defaultGroupId, projectPath.getArtifactId(), projectPath.getVersion(), className, "",sonDomainPomModels,param.getMavenPom());
            // 新增yml配置
            areaProjectNode.buildYml(projectPath.getPropertiesPath(), projectPath.getArtifactId(), projectPath.getVersion(), param.getDatasource(), areaDomain.getAreaName(), areaDomain.getAreaId(),param.getTaskId());
            // 生成参数类
            if (!StringUtils.isEmpty(param.getParamClass())) {
                String paramClassName = ClassUtil.extractClassName(param.getParamClass());
                areaProjectNode.buildParamClass(projectPath.getDataPath(), param.getParamClass(), paramClassName);
            }
            if (!StringUtils.isEmpty(param.getResultClass())) {
                String resultClassName = ClassUtil.extractClassName(param.getResultClass());
                areaProjectNode.buildParamResult(projectPath.getDataPath(), param.getResultClass(), resultClassName);
            }
            Map<String, String> mvnInstallInfo = areaProjectNode.mvnInstall(projectPath.getPomPath());
            if (!mvnInstallInfo.get("code").equals("200")) {
                projectResult.setErrorStackTrace(mvnInstallInfo.get("mvnInstallError"));
                projectResult.setStatus(CreateProjectStatus.INIT_FAIL.name());
                return projectResult;
            }
            // 读取文件目录下的jar包
            Path jar = areaProjectNode.readJarFilesInDirectory(projectPath.getJarFilePath());
            String url = spiderClient.uploadFile(jar);
            projectResult.setBizName(projectPath.getArtifactId());
            projectResult.setBizVersion(projectPath.getVersion());
            projectResult.setBizUrl(url);
            areaDomainFunctionInfoNew.setBizUrl(url);
            areaDomainFunctionInfoNew.setInstance_num(1);
            // 进行部署到宿主应用中
            areaDomainFunctionInfoNew.setStatus(AreaFunctionStatus.INIT_SUSS);
        } catch (Exception e) {
            areaDomainFunctionInfoNew.setStatus(AreaFunctionStatus.INIT_FAIL);
            log.error("init_area_function_fail {}", e.getMessage());
            projectResult.setStatus(CreateProjectStatus.INIT_FAIL.name());
            projectResult.setErrorStackTrace(e.getMessage());
        }
        areaDomainFunctionInfoService.saveOrUpdate(areaDomainFunctionInfoNew);
        projectResult.setId(areaDomainFunctionInfoNew.getId());
        // 调用spider-加载数据
        return projectResult;
    }
}
