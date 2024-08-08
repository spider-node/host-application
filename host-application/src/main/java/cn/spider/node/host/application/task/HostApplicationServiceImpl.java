/*package cn.spider.node.host.application.task;
import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.framework.host.application.base.plugin.data.TaskRequest;
import cn.spider.framework.linker.client.host.HostApplicationService;
import cn.spider.framework.linker.sdk.data.FunctionRequest;
import cn.spider.framework.linker.sdk.data.LinkerServerRequest;
import cn.spider.node.host.application.escalation.EscalationManager;
import org.springframework.beans.BeanUtils;

public class HostApplicationServiceImpl implements HostApplicationService {
    private EscalationManager escalationManager;

    public HostApplicationServiceImpl(EscalationManager escalationManager) {
        this.escalationManager = escalationManager;
    }

    @Override
    public Object runFunction(LinkerServerRequest request) {
        FunctionRequest requestParam = request.getFunctionRequest();
        String pluginKey = requestParam.getComponentName() + requestParam.getServiceName();
        TaskService taskService =escalationManager.queryTaskService(pluginKey);
        TaskRequest taskRequest = new TaskRequest();
        BeanUtils.copyProperties(requestParam,taskRequest);
        return taskService.runTask(taskRequest);
    }
}*/
