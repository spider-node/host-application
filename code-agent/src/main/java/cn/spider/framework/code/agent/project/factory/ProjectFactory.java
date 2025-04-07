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
import cn.spider.framework.code.agent.config.BaseDeployConfig;
import cn.spider.framework.code.agent.data.SonDomainModel;
import cn.spider.framework.code.agent.data.SonDomainModelInfo;
import cn.spider.framework.code.agent.data.SonDomainPomModel;
import cn.spider.framework.code.agent.function.AreaProjectNode;
import cn.spider.framework.code.agent.project.factory.data.InitAreaBaseResult;
import cn.spider.framework.code.agent.project.factory.data.ProjectParam;
import cn.spider.framework.code.agent.project.factory.data.UpdateCoderInfoParam;
import cn.spider.framework.code.agent.project.factory.data.enums.CreateProjectStatus;
import cn.spider.framework.code.agent.project.path.ProjectPathService;
import cn.spider.framework.code.agent.project.path.data.ProjectPath;
import cn.spider.framework.code.agent.spider.SpiderClient;
import cn.spider.framework.code.agent.util.ClassUtil;
import cn.spider.framework.code.agent.util.NumberUtil;
import cn.spider.node.framework.code.agent.sdk.data.CreateProjectResult;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
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

    @Resource
    private BaseDeployConfig baseDeployConfig;


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
                .eq(!StringUtils.isEmpty(param.getVersion()), AreaDomainInfo::getVersion, param.getVersion())
                .last(" order by create_time desc limit 1").one();

        String finalVersion = !StringUtils.isEmpty(param.getVersion()) ? param.getVersion() : Objects.nonNull(areaDomain) ? ClassUtil.getNextVersion(areaDomain.getVersion()) : "";
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
            if (!StringUtils.isEmpty(param.getVersion())) {
                areaDomainInfo.setId(areaDomain.getId());
            }
            areaDomainInfoService.saveOrUpdate(areaDomainInfo);
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
        // 获取出areaDomains中的datasourceName只取一个
        String datasourceName = areaDomains.stream().map(item -> item.getDatasourceName()).distinct().collect(Collectors.joining(","));

        List<SonDomainModel> sonDomainModels = areaDomains.stream().map(item -> {
            SonDomainModel sonDomainModel = new SonDomainModel();
            sonDomainModel.setSonAreaId(item.getSonAreaId());
            sonDomainModel.setSonAreaName(item.getSonAreaName());
            sonDomainModel.setBaseVersion(item.getVersion());
            return sonDomainModel;
        }).collect(Collectors.toList());

        List<SonDomainPomModel> sonDomainPomModels = areaDomains.stream().map(item -> {
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
        Set<String> servicePaths = areaDomains.stream().map(item -> item.getDomainObjectServicePackage()).collect(Collectors.toSet());

        AreaDomainFunctionInfo areaDomainFunctionInfo = areaDomainFunctionInfoService.lambdaQuery()
                .eq(AreaDomainFunctionInfo::getDomainFunctionVersionId, param.getDomainFunctionVersionId())
                .eq(AreaDomainFunctionInfo::getVersion, param.getVersion()).one();
        String bizVersion = Objects.isNull(areaDomainFunctionInfo) ? null : StringUtils.isEmpty(areaDomainFunctionInfo.getBizVersion()) ? "1.0.0" : NumberUtil.upgradeVersion(areaDomainFunctionInfo.getBizVersion());

        // 版本号由外部指定
        ProjectPath projectPath = projectPathService.buildAreaProjectPath(datasourceName, param.getTableName(), bizVersion, className, param.getVersion(), param.getTaskService(), param.getTaskComponent());


        AreaDomainFunctionInfo areaDomainFunctionInfoNew = new AreaDomainFunctionInfo();
        areaDomainFunctionInfoNew.setAreaFunctionClass(param.getServiceClass());

        areaDomainFunctionInfoNew.setDatasourceName(areaDomain.getDatasourceName());
        areaDomainFunctionInfoNew.setTableName(param.getTableName());
        areaDomainFunctionInfoNew.setFunctionName(className);
        areaDomainFunctionInfoNew.setFunctionDesc(param.getDesc());
        areaDomainFunctionInfoNew.setVersion(param.getVersion());
        areaDomainFunctionInfoNew.setStatus(AreaFunctionStatus.INIT);
        areaDomainFunctionInfoNew.setGroupId(projectPath.getGroupId());
        areaDomainFunctionInfoNew.setDatasourceId(areaDomain.getDatasourceId());
        areaDomainFunctionInfoNew.setArtifactId(projectPath.getArtifactId());
        if (Objects.nonNull(param.getInstance())) {
            areaDomainFunctionInfoNew.setInstanceNum(param.getInstance());
        } else {
            areaDomainFunctionInfoNew.setInstanceNum(1);
        }
        if (CollectionUtils.isNotEmpty(param.getOtherCode())) {
            areaDomainFunctionInfoNew.setOtherCode(JSON.toJSONString(param.getOtherCode()));
        }
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
        areaDomainFunctionInfoNew.setBizVersion(bizVersion);

        areaDomainFunctionInfoNew.setId(Objects.nonNull(areaDomainFunctionInfo) ? areaDomainFunctionInfo.getId() : null);
        CreateProjectResult projectResult = new CreateProjectResult();
        try {
            // 生成项目
            areaProjectNode.generateProject(projectPath.getArtifactId(), this.defaultGroupId, projectPath.getArtifactId(), projectPath.getProjectAreaPath(), projectPath.getVersion(), projectPath.getJavaFilePath());
            log.info("构造项目成功");
            // 生成java启动类
            areaProjectNode.buildStart(projectPath.getMainPath(), className + "Start", projectPath.getStartClassPackagePath(), mapperPaths);
            log.info("java启动类构造成功");
            // 生成java 业务类
            areaProjectNode.buildParamClass(projectPath.getServicePath(), serviceClass, className);
            log.info("java业务类完成");
            // 现在配置类- 配置 扫描base的路径,
            areaProjectNode.buildConfig(projectPath.getConfigPath(), projectPath.getConfigPackage(), servicePaths);
            log.info("java配置类完成");
            // 更新pom文件
            areaProjectNode.buildAreaPom(projectPath.getPomPath(), this.defaultGroupId, projectPath.getArtifactId(), projectPath.getVersion(), className, "", sonDomainPomModels, param.getMavenPom(), projectPath.getBizName());
            log.info("pom更新完成");
            // 新增yml配置
            areaProjectNode.buildYml(projectPath.getPropertiesPath(), projectPath.getArtifactId(), areaDomainFunctionInfoNew.getVersion(), areaDomain.getDatasourceName(), areaDomain.getAreaName(), areaDomain.getAreaId(), param.getTaskId(), areaDomainFunctionInfoNew.getBizVersion());
            log.info("pom更新完成");
            // 生成参数类
            List<String> paramClassList = new ArrayList<>();
            List<String> resultClassList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(param.getParamClass())) {
                for (String paramClass : param.getParamClass()) {
                    // 判断 paramClass包含@StaTaskField
                    if (paramClass.contains("@StaTaskField")) {
                        paramClassList.add(paramClass);
                    } else if (paramClass.contains("@NoticeSta")) {
                        resultClassList.add(paramClass);
                    }
                }
            }
            // 写入，入参
            if (CollectionUtils.isNotEmpty(paramClassList)) {
                for (String paramClass : paramClassList) {
                    String paramClassName = ClassUtil.extractClassName(paramClass);
                    areaProjectNode.buildParamClass(projectPath.getDataPath(), paramClass, paramClassName);
                }
            }

            resultClassList = CollectionUtils.isEmpty(resultClassList) ? param.getResultClass() : resultClassList;
            // 写出参
            if (CollectionUtils.isNotEmpty(resultClassList)) {
                for (String resultClass : resultClassList) {
                    String resultClassName = ClassUtil.extractClassName(resultClass);
                    areaProjectNode.buildParamResult(projectPath.getDataPath(), resultClass, resultClassName);
                }
            }

            param.setParamClass(paramClassList);
            param.setResultClass(resultClassList);
            areaDomainFunctionInfoNew.setAreaFunctionParamClass(JSON.toJSONString(param.getParamClass()));
            if (CollectionUtils.isNotEmpty(param.getResultClass())) {
                areaDomainFunctionInfoNew.setAreaFunctionResultClass(JSON.toJSONString(param.getResultClass()));
            }

            if (CollectionUtils.isNotEmpty(param.getOtherCode())) {
                for (String otherCode : param.getOtherCode()) {
                    String otherCodeName = ClassUtil.extractClassName(otherCode);
                    areaProjectNode.buildOtherCode(projectPath.getOtherCodePath(), otherCode, otherCodeName);
                }

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
            // 整理部署的yaml信息
            String deployYaml = areaProjectNode.buildDeploymentYaml(projectPath.getVersion(), url, baseDeployConfig.getDefaultNamespace(), projectPath.getBizName(), areaDomainFunctionInfoNew.getInstanceNum());

            areaDomainFunctionInfoNew.setDeployYaml(deployYaml);
            projectResult.setBizName(projectPath.getArtifactId());
            projectResult.setBizVersion(projectPath.getVersion());
            projectResult.setBizUrl(url);
            areaDomainFunctionInfoNew.setBizUrl(url);

            // 进行部署到宿主应用中
            areaDomainFunctionInfoNew.setStatus(AreaFunctionStatus.INIT_SUSS);
        } catch (Exception e) {
            areaDomainFunctionInfoNew.setStatus(AreaFunctionStatus.INIT_FAIL);
            log.info("init_area_function_fail {}", ExceptionMessage.getStackTrace(e));
            projectResult.setStatus(CreateProjectStatus.INIT_FAIL.name());
            projectResult.setErrorStackTrace(e.getMessage());
        }
        /*param.setServiceClass(null);
        param.setParamClass(null);
        param.setResultClass(null);*/
        areaDomainFunctionInfoNew.setBuildProjectParam(param);
        areaDomainFunctionInfoService.saveOrUpdate(areaDomainFunctionInfoNew);
        projectResult.setId(areaDomainFunctionInfoNew.getId());
        // 调用spider-加载数据
        return projectResult;
    }

    public CreateProjectResult updateDomainFunctionCoder(UpdateCoderInfoParam param) {
        AreaDomainFunctionInfo areaDomainFunctionInfo = areaDomainFunctionInfoService
                .lambdaQuery()
                .eq(AreaDomainFunctionInfo::getDomainFunctionVersionId, param.getDomainFunctionVersionId())
                .one();
        if (Objects.isNull(areaDomainFunctionInfo)) {
            Preconditions.checkArgument(false, "没有找到版本信息");
        }
        ProjectParam projectParam = areaDomainFunctionInfo.getBuildProjectParam();
        if (StringUtils.isNotEmpty(param.getAreaFunctionClass())) {
            projectParam.setServiceClass(param.getAreaFunctionClass());
        }
        if (CollectionUtils.isNotEmpty(param.getAreaFunctionParamClass())) {
            projectParam.setParamClass(param.getAreaFunctionParamClass());
        }
        if (CollectionUtils.isNotEmpty(param.getAreaFunctionResultClass())) {
            projectParam.setResultClass(param.getAreaFunctionResultClass());
        }

        projectParam.setInstance(param.getInstanceNum());
        return createAreaProject(projectParam);
    }
}
