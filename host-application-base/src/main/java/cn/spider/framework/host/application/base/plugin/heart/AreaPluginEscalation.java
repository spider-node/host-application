package cn.spider.framework.host.application.base.plugin.heart;

import cn.spider.framework.host.application.base.heart.EscalationInfo;
import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.host.application.base.plugin.task.SpiderPluginManager;
import cn.spider.framework.host.application.base.plugin.task.data.SpiderPlugin;

import java.util.List;

/**
 * 领域功能插件信息的上报
 */
public class AreaPluginEscalation {
    private HostService hostService;

    private SpiderPluginManager spiderPluginManager;

    private String moduleName;

    private String moduleVersion;

    public AreaPluginEscalation(HostService hostService, SpiderPluginManager spiderPluginManager, String moduleName, String moduleVersion) {
        this.hostService = hostService;
        this.spiderPluginManager = spiderPluginManager;
        this.moduleName = moduleName;
        this.moduleVersion = moduleVersion;
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
            hostService.escalationPlugInInfo(escalationInfo);
        }
    }
}
