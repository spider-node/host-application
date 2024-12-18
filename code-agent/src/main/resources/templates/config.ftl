package ${configPath};
import cn.spider.framework.host.application.base.plugin.config.HostApplicationCenterConfig;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.*;

@Import({HostApplicationCenterConfig.class})
@ComponentScan(basePackages = {<#list baseScanPackage as item>
                                    "${item}",
                                </#list>})
@Configuration
public class AreaApplicationConfig {
}
