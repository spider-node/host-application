package cn.spider.framework.host.application.base.plugin.task;

import cn.spider.framework.host.application.base.plugin.task.data.SpiderPlugin;
import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.framework.host.application.base.plugin.data.TaskRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class TaskServiceImpl implements TaskService {
    private SpiderPluginManager pluginManager;

    private final String heart_info = "Heartbeat detection";

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    public TaskServiceImpl(SpiderPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    // 注意,需要后续新增事务功能
    @Override
    public Object runTask(TaskRequest request) {
        String domainFunctionKey = request.getDomainFunctionKey();
        SpiderPlugin plugin = pluginManager.get(domainFunctionKey);
        if (Objects.isNull(plugin)) {
            Preconditions.checkArgument(false, "没有找打对应的业务方法请检查");
        }
        Object[] params = buildParam(request.getParam(), plugin.getMethod());
        return ReflectionUtils.invokeMethod(plugin.getMethod(), plugin.getTagertObject(), params);
    }

    public Object[] buildParam(Map<String, Object> paramMap, Method method) {
        Parameter[] parameters = method.getParameters();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Object[] params = new Object[parameterNames.length];
        if (parameterNames.length == 1) {
            Class paramType = parameters[0].getType();
            params[0] = JSON.parseObject(JSON.toJSONString(paramMap), paramType);
            return params;
        }
        for (int i = 0; i < parameterNames.length; i++) {
            Parameter parameter = parameters[i];
            String parameterName = parameterNames[i];
            if (!paramMap.containsKey(parameterName)) {
                params[i] = null;
                continue;
            }
            Object requestParam = paramMap.get(parameterName);
            if (Objects.isNull(requestParam)) {
                params[i] = null;
                continue;
            }
            Class paramType = parameter.getType();
            params[i] = JSON.parseObject(JSON.toJSONString(requestParam), paramType);
        }
        return params;
    }
}
