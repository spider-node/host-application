package cn.spider.framework.host.application.base.plugin.heart;

import cn.spider.framework.host.application.base.heart.EscalationInfo;
import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.param.result.build.analysis.SpiderPluginManager;
import cn.spider.framework.param.result.build.model.NodeParamInfoBath;
import cn.spider.framework.param.result.build.model.SpiderPlugin;

import java.util.List;

/**
 * 领域功能插件信息的上报
 */
public class AreaPluginEscalation {
    private HostService hostService;

    private SpiderPluginManager spiderPluginManager;

    private String moduleName;

    private String moduleVersion;

    private String bizVersion;

    public AreaPluginEscalation(HostService hostService, SpiderPluginManager spiderPluginManager, String moduleName, String moduleVersion,String bizVersion) {
        this.hostService = hostService;
        this.spiderPluginManager = spiderPluginManager;
        this.moduleName = moduleName;
        this.moduleVersion = moduleVersion;
        this.bizVersion = bizVersion;
    }

    /**
     * 上报到基座
     */
    public void escalationAreaPluginToBase() {
        List<SpiderPlugin> spiderPlugins = spiderPluginManager.allPlugin();
        for (SpiderPlugin spiderPlugin : spiderPlugins) {
            EscalationInfo escalationInfo = new EscalationInfo();
            escalationInfo.setModuleName(this.moduleName);
            escalationInfo.setModuleVersion(this.moduleVersion);
            escalationInfo.setComponentName(spiderPlugin.getComponentName());
            escalationInfo.setServiceName(spiderPlugin.getServiceName());
            escalationInfo.setMethodName(spiderPlugin.getMethodName());
            escalationInfo.setBizVersion(this.bizVersion);
            // 调用宿主的service进行注册
            hostService.escalationPlugInInfo(escalationInfo);
        }
        // 上传参数信息
        NodeParamInfoBath refreshAreaParam = spiderPluginManager.getNodeParamInfoBath();
        hostService.escalationPlugInParam(refreshAreaParam);
    }
}
