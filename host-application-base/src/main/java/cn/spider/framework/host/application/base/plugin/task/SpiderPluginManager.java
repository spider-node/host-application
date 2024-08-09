package cn.spider.framework.host.application.base.plugin.task;
import cn.spider.framework.annotation.TaskComponent;
import cn.spider.framework.annotation.TaskService;
import cn.spider.framework.host.application.base.plugin.task.data.SpiderPlugin;
import cn.spider.framework.host.application.base.util.ProxyUtil;
import com.alipay.sofa.common.utils.AssertUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class SpiderPluginManager {
    /**
     * 插件中的方法管理
     */
    private Map<String, SpiderPlugin> methodMap;

    private ApplicationContext applicationContext;

    public SpiderPluginManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.methodMap = new HashMap<>();
    }

    public void init(){
        Map<String, Object> beansOfClassAnnotation = applicationContext.getBeansWithAnnotation(TaskComponent.class);
        if(beansOfClassAnnotation.isEmpty()){
            return;
        }
        beansOfClassAnnotation.values().forEach(item->{
            Class<?> targetClass = ProxyUtil.noneProxyClass(item);
            TaskComponent taskComponent = AnnotationUtils.findAnnotation(targetClass, TaskComponent.class);
            String taskComponentName = taskComponent.name();
            // 获取到所有的方法
            Method[] taskServiceMethods = MethodUtils.getMethodsWithAnnotation(targetClass, TaskService.class, false, false);
            List<Method> taskServiceMethodList = filterTaskServiceMethods(taskServiceMethods, targetClass);
            if (CollectionUtils.isEmpty(taskServiceMethodList)) {
                return;
            }
            taskServiceMethodList.forEach(method -> {
                TaskService annotation = method.getAnnotation(TaskService.class);
                AssertUtil.notNull(annotation);
                String taskServiceName = annotation.name();
                String key = taskComponentName+taskServiceName;
                SpiderPlugin spiderPlugin = new SpiderPlugin(method,item,key,taskComponentName,taskServiceName,method.getName());
                register(key,spiderPlugin);
            });
        });
    }

    private List<Method> filterTaskServiceMethods(Method[] taskServiceMethods, Class<?> targetClass) {
        if (ArrayUtils.isEmpty(taskServiceMethods)) {
            return Lists.newArrayList();
        }
        List<Method> taskServiceMethodList = Arrays.stream(taskServiceMethods).filter(tsm -> targetClass.isAssignableFrom(tsm.getDeclaringClass())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskServiceMethodList)) {
            return Lists.newArrayList();
        }

        List<Method> methodList = Lists.newArrayList();
        taskServiceMethodList.stream().collect(Collectors.groupingBy(m -> {
            TaskService annotation = m.getAnnotation(TaskService.class);
            String name = StringUtils.isBlank(annotation.name()) ? StringUtils.uncapitalize(m.getName()) : annotation.name();
            return joinName(name, annotation.ability());
        })).forEach((ts, list) -> {
            if (list.size() <= 1) {
                methodList.add(list.get(0));
                return;
            }
            list.sort((m1, m2) -> {
                if (!m1.getReturnType().isAssignableFrom(m2.getReturnType())) {
                    return -1;
                }
                Class<?>[] p1List = m1.getParameterTypes();
                Class<?>[] p2List = m2.getParameterTypes();
                if (p1List.length == 0) {
                    return 0;
                }
                for (int i = 0; i < p1List.length; i++) {
                    if (!p1List[i].isAssignableFrom(p2List[i])) {
                        return -1;
                    }
                }
                return 0;
            });
            methodList.add(list.get(0));
        });
        return methodList;
    }


    public String joinName(String left, String right) {
        return innerJoin(left, right, "@");
    }

    private String innerJoin(String left, String right, String sign) {
        if (StringUtils.isBlank(right)) {
            return left;
        }
        return left + sign + right;
    }

    public SpiderPlugin get(String key){
        return methodMap.get(key);
    }

    public List<SpiderPlugin> allPlugin(){
        return new ArrayList<>(methodMap.values());
    }

    public void register(String key,SpiderPlugin spiderPlugin) {
        methodMap.put(key,spiderPlugin);
    }


}
