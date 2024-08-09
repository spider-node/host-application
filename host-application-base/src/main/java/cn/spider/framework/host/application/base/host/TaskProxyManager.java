package cn.spider.framework.host.application.base.host;
import cn.spider.framework.host.application.base.plugin.TaskService;
import com.alipay.sofa.koupleless.common.api.SpringServiceFinder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 代理插件得服务信息 宿主机使用- 监听事件
 */
public class TaskProxyManager {
    private Map<String, TaskService> taskServiceMap;


    public TaskProxyManager() {
        this.taskServiceMap = new ConcurrentHashMap<>();
    }

    public void register(String uniqueId) {
        TaskService taskService = SpringServiceFinder.getModuleService("biz", "0.0.1-SNAPSHOT",
                "studentProvider", TaskService.class);
        this.taskServiceMap.put(uniqueId, taskService);
    }

    public TaskService get(String uniqueId) {
        return taskServiceMap.get(uniqueId);
    }

    public void delete(String uniqueId) {
        this.taskServiceMap.remove(uniqueId);
    }
}
