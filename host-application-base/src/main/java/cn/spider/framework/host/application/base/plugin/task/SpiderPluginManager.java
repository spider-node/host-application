package cn.spider.framework.host.application.base.plugin.task;

import cn.spider.framework.host.application.base.plugin.task.data.SpiderPlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SpiderPluginManager {
    /**
     * 插件中的方法管理
     */
    private Map<String, SpiderPlugin> methodMap;

    private ApplicationContext applicationContext;

    public void init(){
    }

    public SpiderPluginManager(){
        this.methodMap = new ConcurrentHashMap<>();
    }

    public SpiderPlugin get(String key){
        return methodMap.get(key);
    }

    public void put(String key,SpiderPlugin spiderPlugin) throws Exception {
        if(methodMap.containsKey(key)){
            throw new Exception("对应key的方法已经存在");
        }
        methodMap.put(key,spiderPlugin);
    }
}
