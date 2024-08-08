package cn.spider.framework.host.application.base.plugin.config;
import cn.spider.framework.host.application.base.host.mysql.DataSourceService;
import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.framework.host.application.base.plugin.task.SpiderPluginManager;
import cn.spider.framework.host.application.base.plugin.task.TaskServiceImpl;
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

/**
 * 获取宿主容器的上下文data
 */
@Configuration
public class HostApplicationCenterConfig {

    @Bean(name = "transactionManager")
    public PlatformTransactionManager platformTransactionManager() {
        DataSourceService dataSourceService = (DataSourceService) getBaseBean("dataSourceService");
        DataSource dataSource = dataSourceService.queryDataSource("");
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "transactionTemplate")
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return  new TransactionTemplate(transactionManager);
    }

    @Bean(name = "sqlFactory")
    public SqlSessionFactoryBean mysqlSqlFactory() throws IOException {
        //数据源不能申明成模块spring上下文中的bean，因为模块卸载时会触发close方法
        DataSourceService dataSourceService = (DataSourceService) getBaseBean("dataSourceService");
        DataSource dataSource = dataSourceService.queryDataSource("");
        SqlSessionFactoryBean mysqlSqlFactory = new SqlSessionFactoryBean();
        mysqlSqlFactory.setDataSource(dataSource);
        mysqlSqlFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mappers/*.xml"));
        return mysqlSqlFactory;
    }

    /**
     * 构造插件层发布的实现类
     * @param pluginManager
     * @return
     */
    @Bean
    public TaskService buildTaskService(SpiderPluginManager pluginManager){
        return new TaskServiceImpl(pluginManager);
    }

    /**
     * 注入插件的管理
     * @return
     */
    @Bean
    public SpiderPluginManager buildSpiderPluginManager(){
        return new SpiderPluginManager();
    }
}
