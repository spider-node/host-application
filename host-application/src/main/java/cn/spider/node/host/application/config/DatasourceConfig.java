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

    private Integer initialSize;

    private Integer minIdle;

    private Integer maxIdle;

    private Integer maxActive;

    private String driverClassName;
}
