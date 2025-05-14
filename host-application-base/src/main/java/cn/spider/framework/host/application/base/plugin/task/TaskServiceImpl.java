package cn.spider.framework.host.application.base.plugin.task;

import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.framework.host.application.base.plugin.data.TaskRequest;
import cn.spider.framework.param.result.build.analysis.SpiderPluginManager;
import cn.spider.framework.param.result.build.model.SpiderPlugin;
import cn.spider.framework.transaction.sdk.context.RootContext;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class TaskServiceImpl implements TaskService {
    private SpiderPluginManager pluginManager;

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    private PlatformTransactionManager transactionManager;

    private final String TRANSACTION_DEFAULT_NAME = "spider-transaction";

    private DefaultTransactionDefinition def;

    public TaskServiceImpl(SpiderPluginManager pluginManager, PlatformTransactionManager transactionManager) {
        this.pluginManager = pluginManager;
        this.transactionManager = transactionManager;
        this.def = new DefaultTransactionDefinition();
        // 设置默认的事务名称
        def.setName(this.TRANSACTION_DEFAULT_NAME);
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    // 注意,需要后续新增事务功能
    @Override
    public Object runTask(TaskRequest request) {
        log.info("runTask-request: {}",JSON.toJSONString(request));
        String domainFunctionKey = request.getDomainFunctionKey();
        SpiderPlugin plugin = pluginManager.get(domainFunctionKey);
        // 获取到插件
        if (Objects.isNull(plugin)) {
            Preconditions.checkArgument(false, "没有找到对应的业务方法请检查");
        }
        Object[] params = buildParam(request.getParam(), plugin.getMethod());
        if (Objects.isNull(request.getBranchId())) {
            return ReflectionUtils.invokeMethod(plugin.getMethod(), plugin.getTagertObject(), params);
        }

        return executeInTransaction(request, plugin, params);
    }

    private Object executeInTransaction(TaskRequest request, SpiderPlugin plugin, Object[] params) {
        log.info("executeInTransaction-request: {}, plugin: {}", request, plugin);
        RootContext.bind(request.getXid());
        RootContext.bindBranchId(request.getBranchId() + "");
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Object result = ReflectionUtils.invokeMethod(plugin.getMethod(), plugin.getTagertObject(), params);
            transactionManager.commit(status);
            RootContext.unbind();
            return result;
        } catch (Exception e) {
            log.error("Transaction failed for request: {}, plugin: {}", request, plugin, e);
            transactionManager.rollback(status);
            RootContext.unbind();
            throw new RuntimeException(e);
        }

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
