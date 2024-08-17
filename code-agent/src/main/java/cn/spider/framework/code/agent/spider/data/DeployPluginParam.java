package cn.spider.framework.code.agent.spider.data;

import lombok.Data;

@Data
public class DeployPluginParam {

    /**
     * 插件名称
     */
    private String bizName;

    /**
     * 插件版本
     */
    private String bizVersion;

    /**
     * 插件的url地址
     */
    private String bizUrl;
}
