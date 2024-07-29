package cn.spider.framework.host.application.base.host;

import cn.spider.framework.host.application.base.base.ClientFactoryBean;
import cn.spider.framework.host.application.base.plugin.TaskService;
import com.alipay.sofa.runtime.api.client.ReferenceClient;
import com.alipay.sofa.runtime.api.client.param.ReferenceParam;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理插件得服务信息 宿主机使用- 监听事件
 */
public class TaskProxyManager {
    private Map<String, TaskService> taskServiceMap;

    private ClientFactoryBean clientFactoryBean;

    public TaskProxyManager(ClientFactoryBean clientFactoryBean) {
        this.clientFactoryBean = clientFactoryBean;
        this.taskServiceMap = new ConcurrentHashMap<>();
    }

    public void register(String uniqueId) {
        ReferenceClient referenceClient = clientFactoryBean.getClientFactory().getClient(ReferenceClient.class);
        ReferenceParam<TaskService> referenceParam = new ReferenceParam<>();
        referenceParam.setInterfaceType(TaskService.class);
        referenceParam.setUniqueId(uniqueId);
        TaskService proxy = referenceClient.reference(referenceParam);
        this.taskServiceMap.put(uniqueId, proxy);
    }

    public TaskService get(String uniqueId) {
        return taskServiceMap.get(uniqueId);
    }

    public void delete(String uniqueId) {
        ReferenceParam<TaskService> referenceParam = new ReferenceParam<>();
        referenceParam.setInterfaceType(TaskService.class);
        referenceParam.setUniqueId(uniqueId);
        ReferenceClient referenceClient = clientFactoryBean.getClientFactory().getClient(ReferenceClient.class);
        referenceClient.removeReference(referenceParam);
        this.taskServiceMap.remove(uniqueId);
    }
}
