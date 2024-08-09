package cn.spider.node.host.application.escalation;

import cn.spider.framework.host.application.base.heart.EscalationInfo;
import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.node.host.application.escalation.data.PluginInfo;
import com.alipay.sofa.koupleless.common.api.SpringServiceFinder;


/**
 * 用于接受插件的上报
 */
public class HostServiceImpl implements HostService {

    private EscalationManager escalationManager;
    public HostServiceImpl(EscalationManager escalationManager) {
        this.escalationManager = escalationManager;
    }

    @Override
    public void escalationPlugInInfo(EscalationInfo escalationInfo) {
        TaskService taskService = SpringServiceFinder.getModuleService(escalationInfo.getModuleName(), escalationInfo.getModuleVersion(),
                "taskService", TaskService.class);
        PluginInfo pluginInfo = new PluginInfo(escalationInfo,taskService);
        escalationManager.registerPluginInfo(escalationInfo.getUniqueId(),pluginInfo);
    }
}
