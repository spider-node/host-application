package cn.spider.node.host.application.config;

import cn.spider.framework.host.application.base.host.mysql.DataSourceService;
import cn.spider.node.host.application.source.DataSourceServiceImpl;
import cn.spider.node.host.application.source.SourceManager;
import cn.spider.node.host.application.source.service.IAreaDatasourceInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {

    @Bean
    public SourceManager buildSourceManager(IAreaDatasourceInfoService datasourceInfoService){
        return new SourceManager(datasourceInfoService);
    }

    @Bean
    public DataSourceService buildDataSourceService(SourceManager sourceManager){
        return new DataSourceServiceImpl(sourceManager);
    }
}
