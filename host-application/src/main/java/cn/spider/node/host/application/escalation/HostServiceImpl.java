package cn.spider.node.host.application.escalation;

import cn.spider.framework.host.application.base.heart.EscalationInfo;
import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.framework.host.application.base.plugin.param.RefreshAreaParam;
import cn.spider.framework.linker.client.socket.SocketManager;
import cn.spider.framework.linker.sdk.data.emuns.FunctionEscalationType;
import cn.spider.node.host.application.escalation.data.PluginInfo;
import com.alibaba.fastjson.JSON;
import com.alipay.sofa.koupleless.common.api.SpringServiceFinder;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 用于接受插件的上报
 */
@Slf4j
public class HostServiceImpl implements HostService {
    private EscalationManager escalationManager;

    // 缓存基座中的插件信息
    private Map<String, RefreshAreaParam> areaFunctionMap;

    private SocketManager socketManager;

    private ApplicationContext context;

    public HostServiceImpl(EscalationManager escalationManager, ApplicationContext context) {
        this.escalationManager = escalationManager;
        this.areaFunctionMap = new ConcurrentHashMap<>();
        this.context = context;

    }

    public void init(){
        if(Objects.nonNull(socketManager)){
            return;
        }
        this.socketManager = context.getBean(SocketManager.class);
    }

    @Override
    public void escalationPlugInInfo(EscalationInfo escalationInfo) {
        TaskService taskService = SpringServiceFinder.getModuleService(escalationInfo.getModuleName(), escalationInfo.getModuleVersion(),
                "taskService", TaskService.class);
        PluginInfo pluginInfo = new PluginInfo(escalationInfo, taskService);
        escalationManager.registerPluginInfo(escalationInfo.getUniqueId(), pluginInfo);
    }

    /**
     * 上报插件信息
     *
     * @param areaFunctionParam
     */
    @Override
    public void escalationPlugInParam(RefreshAreaParam areaFunctionParam) {
        areaFunctionMap.put(areaFunctionParam.getPluginKey(), areaFunctionParam);
        // 告知spider
        log.info("notify_spider_info {}", JSON.toJSONString(areaFunctionParam));
        socketManager.escalationAreaFunctionInfo(JsonObject.mapFrom(areaFunctionParam), FunctionEscalationType.DEPLOY);
    }

    /**
     * 卸载基座的插件
     *
     * @param key 由bizName+version
     */
    @Override
    public void unloadPlugin(String key) {
        if (areaFunctionMap.isEmpty()) {
            return;
        }
        RefreshAreaParam areaParam = this.areaFunctionMap.get(key);
        socketManager.escalationAreaFunctionInfo(JsonObject.mapFrom(areaParam), FunctionEscalationType.DEPLOY);
        areaFunctionMap.remove(key);
    }

    @Override
    public void escalationPlugInAll() {
        for(RefreshAreaParam refreshAreaParam : areaFunctionMap.values()){
            socketManager.escalationAreaFunctionInfo(JsonObject.mapFrom(refreshAreaParam), FunctionEscalationType.DEPLOY);
        }
    }
}
