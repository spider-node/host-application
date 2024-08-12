package cn.spider.framework.host.application.base.plugin;

import cn.spider.framework.host.application.base.plugin.data.TaskRequest;

/**
 * 每个插件应用进行实现
 */
public interface TaskService {
    Object runTask(TaskRequest request);
}
