package cn.spider.node.host.application.task;

import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.framework.host.application.base.plugin.data.TaskRequest;
import cn.spider.framework.host.application.base.util.ComponentUtil;
import cn.spider.framework.linker.client.host.HostApplicationService;
import cn.spider.framework.linker.sdk.data.FunctionRequest;
import cn.spider.framework.linker.sdk.data.LinkerServerRequest;
import cn.spider.node.host.application.escalation.EscalationManager;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class HostApplicationServiceImpl implements HostApplicationService {
    private EscalationManager escalationManager;

    public HostApplicationServiceImpl(EscalationManager escalationManager) {
        this.escalationManager = escalationManager;
    }

    /**
     * 调用插件应用
     *
     * @param request
     * @return 执行后领域对象返回的参数
     */
    @Override
    public Object runFunction(LinkerServerRequest request) {
        FunctionRequest requestParam = request.getFunctionRequest();
        String pluginKey = ComponentUtil.buildComponentKey(requestParam.getComponentName(), requestParam.getServiceName(),requestParam.getVersion());
        TaskService taskService = escalationManager.queryTaskService(pluginKey);
        TaskRequest taskRequest = new TaskRequest();
        // TODO 重新构建插件信息
        taskRequest.setParam(requestParam.getParam());
        taskRequest.setRequestId(requestParam.getRequestId());
        taskRequest.setXid(requestParam.getXid());
        taskRequest.setBranchId(requestParam.getBranchId());
        taskRequest.setComponentName(requestParam.getComponentName());
        taskRequest.setServiceName(requestParam.getServiceName());
        taskRequest.setMethodName(requestParam.getMethodName());
        taskRequest.setVersion(requestParam.getVersion());
        return taskService.runTask(taskRequest);
    }
}
