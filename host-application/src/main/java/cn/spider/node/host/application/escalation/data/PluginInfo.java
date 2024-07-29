package cn.spider.node.host.application.escalation.data;

import cn.spider.framework.host.application.base.host.data.EscalationInfo;
import cn.spider.framework.host.application.base.plugin.TaskService;

public class PluginInfo {
    private EscalationInfo escalationInfo;

    private TaskService taskService;

    public PluginInfo(EscalationInfo escalationInfo, TaskService taskService) {
        this.escalationInfo = escalationInfo;
        this.taskService = taskService;
    }

    public EscalationInfo getEscalationInfo() {
        return escalationInfo;
    }

    public void setEscalationInfo(EscalationInfo escalationInfo) {
        this.escalationInfo = escalationInfo;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void setTaskService(TaskService taskService) {
        this.taskService = taskService;
    }
}
