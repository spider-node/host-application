package cn.spider.node.host.application.test;

import cn.spider.framework.annotation.TaskComponent;
import cn.spider.framework.annotation.TaskService;
import cn.spider.framework.host.application.base.util.ProxyUtil;
import com.alipay.sofa.common.utils.AssertUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class InitComponent {
    @Resource
    private ApplicationContext applicationContext;

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

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
                String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
                Object[] params = new Object[parameterNames.length];
                params[0] = "12555";
                ReflectionUtils.invokeMethod(method, item, params);
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
}
