package cn.spider.framework.host.application.base.plugin.param.analysis;

import cn.spider.framework.annotation.TaskComponent;
import cn.spider.framework.annotation.TaskInstruct;
import cn.spider.framework.annotation.TaskService;
import cn.spider.framework.common.config.Constant;
import cn.spider.framework.host.application.base.plugin.param.*;
import cn.spider.framework.host.application.base.plugin.task.data.SpiderPlugin;
import com.google.common.collect.Lists;
import io.vertx.core.json.JsonObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

;

/**
 * 解析动态价值的url
 */
public class AnalysisClass {

    public Map<String, Map<String, Object>> doInit(Class<?> targetClass) {
        TaskComponent taskComponent = targetClass.getAnnotation(TaskComponent.class);
        Method[] taskServiceMethods = MethodUtils.getMethodsWithAnnotation(targetClass, TaskService.class, false, false);
        List<Method> taskServiceMethodList = filterTaskServiceMethods(taskServiceMethods, targetClass, true);
        if (CollectionUtils.isEmpty(taskServiceMethodList)) {
            return null;
        }
        Map<String, Map<String, Object>> allMapping = new HashMap<>();
        taskServiceMethodList.forEach(method -> {
            Map<String, Object> mapping = new HashMap<>();
            TaskService annotation = method.getAnnotation(TaskService.class);
            String functionName = annotation.functionName();
            String desc = annotation.desc();
            String taskServiceName = StringUtils.isBlank(annotation.name()) ? method.getName() : annotation.name();
            TaskInstructWrapper taskInstruct = getTaskInstructWrapper(method, taskServiceName).orElse(null);
            NoticeAnnotationWrapper noticeMethodSpecify = new NoticeAnnotationWrapper(method);
            MethodWrapper methodWrapper = new MethodWrapper(method, annotation, noticeMethodSpecify, taskInstruct, true);
            List<ParamInjectDef> paramInjectDefsList = new ArrayList<>();
            methodWrapper.getReturnTypeNoticeDef().getNoticeStaDefSet().stream().forEach(item -> {
                String targetName = item.getTargetName();
                List<ParamInjectDef> paramInjectDefs = new ArrayList<>();
                // 包含. targetName说明本身已经是不需要组装域参数信息了
                if(targetName.contains(Constant.SPOT)){
                    ParamInjectDef parameter = new ParamInjectDef(item.getFieldName(), targetName);
                    paramInjectDefs.add(parameter);
                    paramInjectDefsList.addAll(paramInjectDefs);
                    return;
                }
                Field[] fields = item.getFieldClass().getDeclaredFields();
                if (fields.length > 0) {

                    for (Field field : fields) {
                        ParamInjectDef parameter = new ParamInjectDef(field.getName(), targetName + "." + field.getName());
                        paramInjectDefs.add(parameter);
                    }
                    paramInjectDefsList.addAll(paramInjectDefs);
                }
            });
            Object params = CollectionUtils.isEmpty(methodWrapper.getParamInjectDefs()) ? null : methodWrapper.getParamInjectDefs().get(0).getFieldInjectDefList();
            // 构造入参
            JsonObject paramObject = new JsonObject();
            paramObject.put("nodeParamConfigs", params);
            // 构造出参
            JsonObject resultObject = new JsonObject();
            resultObject.put("nodeParamConfigs", paramInjectDefsList);

            mapping.put("param", paramObject);
            mapping.put("result", resultObject);
            mapping.put("method",methodWrapper.getMethod().getName());
            if(StringUtils.isNotEmpty(functionName)){
                mapping.put("functionName",functionName);
            }
            if(StringUtils.isNotEmpty(desc)){
                mapping.put("desc",desc);
            }
            // 改造获取入参,请求参数
            allMapping.put(taskComponent.name() + "@" + taskServiceName, mapping);
        });
        return allMapping;
    }


    public List<Method> filterTaskServiceMethods(Method[] taskServiceMethods, Class<?> targetClass, boolean scanSuper) {
        if (ArrayUtils.isEmpty(taskServiceMethods)) {
            return Lists.newArrayList();
        }
        List<Method> taskServiceMethodList = Arrays.stream(taskServiceMethods).filter(tsm -> {
            if (scanSuper) {
                return true;
            }
            return targetClass.isAssignableFrom(tsm.getDeclaringClass());
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(taskServiceMethodList)) {
            return Lists.newArrayList();
        }

        List<Method> methodList = Lists.newArrayList();
        taskServiceMethodList.stream().collect(Collectors.groupingBy(m -> {
            TaskService annotation = m.getAnnotation(TaskService.class);
            String name = StringUtils.isBlank(annotation.name()) ? StringUtils.uncapitalize(m.getName()) : annotation.name();
            return TaskServiceUtil.joinName(name, annotation.ability());
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

    private Optional<TaskInstructWrapper> getTaskInstructWrapper(Method method, String taskService) {
        TaskInstruct annotation = method.getAnnotation(TaskInstruct.class);
        if (annotation == null) {
            return Optional.empty();
        }
        return Optional.of(new TaskInstructWrapper(annotation, taskService));
    }
}
