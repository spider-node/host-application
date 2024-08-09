package cn.spider.framework.host.application.base.plugin.config;
import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.host.application.base.host.mysql.DataSourceService;
import cn.spider.framework.host.application.base.plugin.TaskService;
import cn.spider.framework.host.application.base.plugin.heart.AreaPluginEscalation;
import cn.spider.framework.host.application.base.plugin.task.SpiderPluginManager;
import cn.spider.framework.host.application.base.plugin.task.TaskServiceImpl;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
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
    public PlatformTransactionManager platformTransactionManager(@Value("${spider.datasource.name}") String datasourceName) {
        DataSourceService dataSourceService = (DataSourceService) getBaseBean("dataSourceService");
        DataSource dataSource = dataSourceService.queryDataSource(datasourceName);
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "transactionTemplate")
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return  new TransactionTemplate(transactionManager);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "sqlFactory")
    public SqlSessionFactory mysqlSqlFactory(@Value("${spider.datasource.name}") String datasourceName) throws Exception {
        //数据源不能申明成模块spring上下文中的bean，因为模块卸载时会触发close方法
        DataSourceService dataSourceService = (DataSourceService) getBaseBean("dataSourceService");
        DataSource dataSource = dataSourceService.queryDataSource(datasourceName);
        MybatisSqlSessionFactoryBean mysqlSqlFactory = new MybatisSqlSessionFactoryBean();
        mysqlSqlFactory.setDataSource(dataSource);
        return mysqlSqlFactory.getObject();
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
    public SpiderPluginManager buildSpiderPluginManager(ApplicationContext applicationContext){
        return new SpiderPluginManager(applicationContext);
    }

    /**
     * 注入领域插件
     * @param spiderPluginManager
     * @param moduleName
     * @param moduleVersion
     * @return
     */
    @Bean
    public AreaPluginEscalation buildAreaPluginEscalation(SpiderPluginManager spiderPluginManager, @Value("${spider.pom.artifactId}") String moduleName, @Value("${spider.pom.version}")String moduleVersion){
        HostService hostService = (HostService)getBaseBean("hostService");
        return new AreaPluginEscalation(hostService,spiderPluginManager,moduleName,moduleVersion);
    }

    /**
     * 容器启动完成后执行该方法
     * @param spiderPluginManager
     * @param areaPluginEscalation
     * @return
     */
    @Bean
    public ApplicationRunner runner(SpiderPluginManager spiderPluginManager, AreaPluginEscalation areaPluginEscalation) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args){
                // 在这里执行你的初始化代码
                spiderPluginManager.init();
                areaPluginEscalation.escalationAreaPluginToBase();
            }
        };
    }
}
