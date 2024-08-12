package cn.spider.node.host.application.escalation;

import cn.spider.framework.host.application.base.heart.EscalationInfo;
import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.framework.host.application.base.plugin.param.RefreshAreaParam;
import cn.spider.node.host.application.escalation.data.PluginInfo;
import com.alibaba.fastjson.JSON;
import com.alipay.sofa.koupleless.common.api.SpringServiceFinder;
import lombok.extern.slf4j.Slf4j;


/**
 * 用于接受插件的上报
 */
@Slf4j
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

    @Override
    public void escalationPlugInParam(RefreshAreaParam areaFunctionParam) {
        // 告知spider
        log.info("notify_spider_info {}", JSON.toJSONString(areaFunctionParam));
    }
}
