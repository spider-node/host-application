package cn.spider.framework.host.application.base.plugin.task;
import cn.spider.framework.annotation.TaskComponent;
import cn.spider.framework.annotation.TaskService;
import cn.spider.framework.host.application.base.plugin.param.RefreshAreaModel;
import cn.spider.framework.host.application.base.plugin.param.RefreshAreaParam;
import cn.spider.framework.host.application.base.plugin.param.analysis.AnalysisClass;
import cn.spider.framework.host.application.base.plugin.task.data.SpiderPlugin;
import cn.spider.framework.host.application.base.util.PluginKeyUtil;
import cn.spider.framework.host.application.base.util.ProxyUtil;
import com.alipay.sofa.common.utils.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class SpiderPluginManager {
    /**
     * 插件中的方法管理
     */
    private Map<String, SpiderPlugin> methodMap;

    private RefreshAreaParam areaFunctionParam;

    private ApplicationContext applicationContext;

    private AnalysisClass analysisClass;

    private String bizName;

    private String version;

    private String areaId;

    public SpiderPluginManager(ApplicationContext applicationContext,String bizName,String version,String areaId) {
        this.applicationContext = applicationContext;
        this.methodMap = new HashMap<>();
        this.analysisClass = new AnalysisClass();
        this.areaFunctionParam = new RefreshAreaParam();
        this.bizName = bizName;
        this.version = version;
        this.areaId = areaId;
    }

    public void init() {
        Map<String, Object> beansOfClassAnnotation = applicationContext.getBeansWithAnnotation(TaskComponent.class);
        if (beansOfClassAnnotation.isEmpty()) {
            return;
        }
        Map<String, Map<String, Object>> refreshAreaParam = new HashMap<>();
        beansOfClassAnnotation.values().forEach(item -> {
            Class<?> targetClass = ProxyUtil.noneProxyClass(item);
            TaskComponent taskComponent = AnnotationUtils.findAnnotation(targetClass, TaskComponent.class);
            String taskComponentName = taskComponent.name();
            // 获取到所有的方法
            Method[] taskServiceMethods = MethodUtils.getMethodsWithAnnotation(targetClass, TaskService.class, false, false);
            List<Method> taskServiceMethodList = analysisClass.filterTaskServiceMethods(taskServiceMethods, targetClass, true);
            if (CollectionUtils.isEmpty(taskServiceMethodList)) {
                return;
            }
            taskServiceMethodList.forEach(method -> {
                TaskService annotation = method.getAnnotation(TaskService.class);
                AssertUtil.notNull(annotation);
                String taskServiceName = annotation.name();
                String key = taskComponentName + taskServiceName;
                // 解析出，入参，出参
                SpiderPlugin spiderPlugin = new SpiderPlugin(method, item, key, taskComponentName, taskServiceName, method.getName());
                register(key, spiderPlugin);
            });
            Map<String, Map<String, Object>> params = analysisClass.doInit(targetClass);
            refreshAreaParam.putAll(params);

        });

        try {
            List<RefreshAreaModel> areaModelList = new ArrayList<>();
            refreshAreaParam.forEach((key, value) -> {
                String[] assembly = key.split("@");
                RefreshAreaModel refreshAreaModel = new RefreshAreaModel();
                refreshAreaModel.setTaskComponent(assembly[0]);
                refreshAreaModel.setTaskService(assembly[1]);
                refreshAreaModel.setParmMap(value);
                refreshAreaModel.setVersion(this.version);
                refreshAreaModel.setAreaId(this.areaId);
                areaModelList.add(refreshAreaModel);
            });
            areaFunctionParam.setAreaModelList(areaModelList);
            areaFunctionParam.setPluginKey(PluginKeyUtil.buildPluginKey(this.bizName,this.version));
        } catch (Exception e) {
            log.error("获取参数失败");
        }

    }

    public SpiderPlugin get(String key) {
        return methodMap.get(key);
    }

    public List<SpiderPlugin> allPlugin() {
        return new ArrayList<>(methodMap.values());
    }

    public void register(String key, SpiderPlugin spiderPlugin) {
        methodMap.put(key, spiderPlugin);
    }

    public RefreshAreaParam queryRefreshAreaParam() {
        return this.areaFunctionParam;
    }
}
