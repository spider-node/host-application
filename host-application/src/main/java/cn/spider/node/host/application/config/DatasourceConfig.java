package cn.spider.node.host.application.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "spider.datasource")
public class DatasourceConfig {
    private String url;

    private String userName;

    private String password;

    private int initialSize;

    private int minIdle;

    private int maxIdle;

    private String driverClassName;
}
