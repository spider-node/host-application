package cn.spider.node.host.application.config;

import cn.spider.framework.host.application.base.host.TaskProxyManager;
import cn.spider.framework.host.application.base.host.mysql.DataSourceService;
import cn.spider.node.host.application.source.DataSourceServiceImpl;
import cn.spider.node.host.application.source.SourceManager;
import cn.spider.node.host.application.source.service.IAreaDatasourceInfoService;
import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.io.IOException;

import static com.alipay.sofa.koupleless.common.api.SpringBeanFinder.getBaseBean;

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

    @Bean
    public TaskProxyManager buildTaskProxyManager(){
        return new TaskProxyManager();
    }


}
