package cn.spider.framework.code.agent.function;

import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitParam;
import cn.spider.framework.code.agent.data.DeployAreaFunctionParam;
import cn.spider.framework.code.agent.project.factory.ProjectFactory;
import cn.spider.framework.code.agent.project.factory.data.ProjectParam;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
public class FunctionManager {
    @Resource
    private ProjectFactory projectFactory;

    /**
     * 目录结构为 root/数据源/领域表名/版本/ 这个空间就是进行mvn生成模板项目
     *
     * @param param
     */
    public void buildProject(DeployAreaFunctionParam param) {
        // 获取项目的名称 名称的命名为 area + functionName
        // 从 serviceClass中获取方法的名称
        try {
            ProjectParam projectParam = new ProjectParam();
            projectParam.setParamClass(param.getParamClass());
            projectParam.setServiceClass(param.getServiceClass());
            projectParam.setDatasource("test_order");
            projectParam.setAreaName("goods");
            projectParam.setTableName("goods");
            projectFactory.createAreaProject(projectParam);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 通过url进行部署到宿主机
    }

    public void initBaseProject(AreaDomainInitParam param) {
        projectFactory.createBaseProject(param);
    }

    // 卸载功能

    // 替换功能
}
