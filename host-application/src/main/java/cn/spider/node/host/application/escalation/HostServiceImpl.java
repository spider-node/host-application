package cn.spider.node.host.application.escalation;

import cn.spider.framework.host.application.base.heart.EscalationInfo;
import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.framework.host.application.base.util.ComponentUtil;
import cn.spider.framework.linker.client.socket.SocketManager;
import cn.spider.framework.linker.sdk.data.emuns.FunctionEscalationType;
import cn.spider.framework.param.result.build.model.NodeParamInfoBath;
import cn.spider.framework.param.result.build.model.ReportParamInfo;
import cn.spider.node.host.application.escalation.data.PluginInfo;
import com.alibaba.fastjson.JSON;
import com.alipay.sofa.koupleless.common.api.SpringServiceFinder;

import com.google.common.collect.Lists;
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
    private Map<String, NodeParamInfoBath> areaFunctionMap;

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
        log.info("插件上报的信息为 {}", JSON.toJSONString(pluginInfo));
        String pluginKey = ComponentUtil.buildComponentKey(escalationInfo.getComponentName(), escalationInfo.getServiceName(),escalationInfo.getModuleVersion());
        escalationManager.registerPluginInfo(pluginKey, pluginInfo);
    }

    /**
     * 上报插件信息
     *
     * @param areaFunctionParam
     */
    @Override
    public void escalationPlugInParam(NodeParamInfoBath areaFunctionParam) {
        areaFunctionMap.put(areaFunctionParam.getPluginKey(), areaFunctionParam);
        // 告知spider
        log.info("notify_spider_info {}", JSON.toJSONString(areaFunctionParam));
        ReportParamInfo reportParamInfo = new ReportParamInfo();
        reportParamInfo.setNodeParamInfoBathList(Lists.newArrayList(areaFunctionParam));
        socketManager.escalationAreaFunctionInfo(JsonObject.mapFrom(reportParamInfo), FunctionEscalationType.DEPLOY);
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
        NodeParamInfoBath areaParam = this.areaFunctionMap.get(key);
        ReportParamInfo reportParamInfo = new ReportParamInfo();
        reportParamInfo.setNodeParamInfoBathList(Lists.newArrayList(areaParam));
        socketManager.escalationAreaFunctionInfo(JsonObject.mapFrom(reportParamInfo), FunctionEscalationType.DEPLOY);
        areaFunctionMap.remove(key);
    }

    @Override
    public void escalationPlugInAll() {
        // 改造成全部上报
        ReportParamInfo reportParamInfo = new ReportParamInfo();
        reportParamInfo.setNodeParamInfoBathList(Lists.newArrayList(areaFunctionMap.values()));
        socketManager.escalationAreaFunctionInfo(JsonObject.mapFrom(reportParamInfo), FunctionEscalationType.DEPLOY);
    }
}
