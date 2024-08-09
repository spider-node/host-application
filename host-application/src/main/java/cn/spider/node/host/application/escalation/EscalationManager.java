package cn.spider.node.host.application.escalation;

import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.node.host.application.escalation.data.PluginInfo;

import java.util.HashMap;
import java.util.Map;

public class EscalationManager {
    private Map<String, PluginInfo> pluginInfoMap;

    public EscalationManager() {
        this.pluginInfoMap = new HashMap<>();
    }

    public void registerPluginInfo(String pluginKey, PluginInfo pluginInfo) {
        this.pluginInfoMap.put(pluginKey,pluginInfo);
    }

    public TaskService queryTaskService(String pluginKey){
        return this.pluginInfoMap.get(pluginKey).getTaskService();
    }
}
