package cn.spider.framework.code.agent.function;

import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitParam;
import cn.spider.framework.code.agent.data.DeployAreaFunctionParam;
import cn.spider.framework.code.agent.project.factory.ProjectFactory;
import cn.spider.framework.code.agent.project.factory.data.InitAreaBaseResult;
import cn.spider.framework.code.agent.project.factory.data.ProjectParam;
import cn.spider.framework.code.agent.project.factory.data.UpdateCoderInfoParam;
import cn.spider.framework.common.utils.ExceptionMessage;
import cn.spider.node.framework.code.agent.sdk.data.CreateProjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;


@Slf4j
@Component
public class FunctionManager {
    @Resource
    private ProjectFactory projectFactory;

    /**
     * 目录结构为 root/数据源/领域表名/版本/ 这个空间就是进行mvn生成模板项目
     *
     * @param param
     */
    public CreateProjectResult buildProject(DeployAreaFunctionParam param) {
        // 获取项目的名称 名称的命名为 area + functionName
        // 从 serviceClass中获取方法的名称
        try {
            ProjectParam projectParam = new ProjectParam();
            projectParam.setParamClass(param.getParamClass());
            projectParam.setResultClass(param.getResultClass());
            projectParam.setServiceClass(param.getServiceClass());
            projectParam.setOtherCode(param.getOtherCode());
            projectParam.setAreaName(param.getAreaName());
            projectParam.setTableName(param.getTableName());
            projectParam.setBaseInfoId(param.getBaseInfoId());
            projectParam.setTaskId(param.getTaskId());
            projectParam.setTaskService(param.getTaskService());
            projectParam.setTaskComponent(param.getTaskComponent());
            projectParam.setVersion(param.getVersion());
            projectParam.setDomainFunctionVersionId(param.getDomainFunctionVersionId());
            projectParam.setMavenPom(param.getMavenPom());
            projectParam.setInstance(param.getInstance());
            return projectFactory.createAreaProject(projectParam);
        } catch (Exception e) {
            log.info("创建项目失败 {}", ExceptionMessage.getStackTrace(e));
            throw new RuntimeException(e);
        }
        // 通过url进行部署到宿主机
    }

    public InitAreaBaseResult initBaseProject(AreaDomainInitParam param) {
        return projectFactory.initBaseProject(param);
    }

    public CreateProjectResult updateDomainFunctionCoder(UpdateCoderInfoParam param) {
        return projectFactory.updateDomainFunctionCoder(param);
    }
}
