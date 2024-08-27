package cn.spider.framework.code.agent.project.factory.data;
import cn.spider.framework.code.agent.areabase.modules.function.entity.AreaDomainFunctionInfo;
import cn.spider.framework.code.agent.project.factory.data.enums.CreateProjectStatus;
import lombok.Data;

@Data
public class CreateProjectResult {
    /**
     * 部署code
     */
    private CreateProjectStatus status;

    /**
     * 部署过程异常
     */
    private String errorStackTrace;

    /**
     * 插件名称
     */
    private String bizName;

    /**
     * 插件版本
     */
    private String bizVersion;

    /**
     * 插件的url地址
     */
    private String bizUrl;

    /**
     * 插件信息
     */
    private AreaDomainFunctionInfo pluginInfo;


}
