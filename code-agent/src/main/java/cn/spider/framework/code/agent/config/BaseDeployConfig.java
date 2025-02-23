package cn.spider.framework.code.agent.config;

public class BaseDeployConfig {
    private String defaultNamespace;

    private String baseName;

    public BaseDeployConfig(String defaultNamespace, String baseName) {
        this.defaultNamespace = defaultNamespace;
        this.baseName = baseName;
    }

    public String getDefaultNamespace() {
        return defaultNamespace;
    }

    public String getBaseName() {
        return baseName;
    }
}
