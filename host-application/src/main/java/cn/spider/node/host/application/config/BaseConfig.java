package cn.spider.node.host.application.config;

import cn.spider.framework.client.config.SpiderClintConfig;
import cn.spider.framework.host.application.base.host.TaskProxyManager;
import cn.spider.framework.host.application.base.host.heart.HostService;
import cn.spider.framework.host.application.base.host.mysql.DataSourceService;
import cn.spider.framework.linker.client.config.CommonConfig;
import cn.spider.framework.linker.client.config.GrpcConfig;
import cn.spider.framework.linker.client.escalation.AreaInfoService;
import cn.spider.framework.linker.client.host.HostApplicationService;
import cn.spider.node.host.application.area.AreaInfoServiceImpl;
import cn.spider.node.host.application.escalation.EscalationManager;
import cn.spider.node.host.application.escalation.HostServiceImpl;
import cn.spider.node.host.application.source.DataSourceServiceImpl;
import cn.spider.node.host.application.source.SourceManager;
import cn.spider.node.host.application.source.service.IAreaDatasourceInfoService;
import cn.spider.node.host.application.task.HostApplicationServiceImpl;
import cn.spider.node.host.application.timer.DataSourceTimer;
import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Import({CommonConfig.class, GrpcConfig.class, SpiderClintConfig.class})
@Configuration
public class BaseConfig {
    @Bean("sourceManager")
    public SourceManager buildSourceManager(IAreaDatasourceInfoService datasourceInfoService) {
        return new SourceManager(datasourceInfoService);
    }

    @Bean
    public DataSourceTimer buildDataSourceTimer(Vertx vertx, SourceManager sourceManager) {
        return new DataSourceTimer(vertx, sourceManager);
    }

    @Bean
    public Vertx buildVertx() {
        VertxOptions options = new VertxOptions();
        options.setWorkerPoolSize(4);
        return Vertx.vertx(options);
    }

    @Bean("dataSourceService")
    public DataSourceService buildDataSourceService(SourceManager sourceManager) {
        return new DataSourceServiceImpl(sourceManager);
    }

    @Bean
    public TaskProxyManager buildTaskProxyManager() {
        return new TaskProxyManager();
    }

    @Bean(name = "transactionManager")
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "transactionTemplate")
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {

        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
        factory.setDataSource(dataSource);
        // 可以在这里配置其他属性，如mapperLocations、configuration等
        return factory.getObject();
    }

    @Bean
    public DataSource dataSource(DatasourceConfig datasourceInfo) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(datasourceInfo.getUrl());
        dataSource.setUsername(datasourceInfo.getUserName());
        dataSource.setPassword(datasourceInfo.getPassword());
        dataSource.setDriverClassName(datasourceInfo.getDriverClassName());
        // 配置初始化大小、最小、最大
        dataSource.setInitialSize(datasourceInfo.getInitialSize());
        dataSource.setMinIdle(datasourceInfo.getMinIdle());
        dataSource.setMaxActive(datasourceInfo.getMaxActive());
        // 配置获取连接等待超时的时间
        dataSource.setMaxWait(60000);

        // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setTimeBetweenEvictionRunsMillis(60000);

        // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(300000);

        // 用来测试连接是否有效的SQL
        dataSource.setValidationQuery("SELECT 1");

        // 建议配置为true，不影响性能，并且保证安全性
        dataSource.setTestWhileIdle(true);

        // 配置监控统计拦截的filters
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);

        // 打开PSCache，并且指定每个连接上PSCache的大小
        dataSource.setPoolPreparedStatements(true);
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

        return dataSource;
    }

    /**
     * 容器初始化完成后执行的方法
     *
     * @param sourceManager
     * @return ApplicationRunner
     */
    @Bean
    public ApplicationRunner runner(SourceManager sourceManager, HostService hostService) {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                hostService.init();
                // 在这里执行你的初始化代码
                sourceManager.initDataSource();
            }
        };
    }

    @Bean("hostService")
    public HostService buildHostService(EscalationManager escalationManager, ApplicationContext applicationContext) {
        return new HostServiceImpl(escalationManager, applicationContext);
    }

    @Bean("escalationManager")
    public EscalationManager buildEscalationManager() {
        return new EscalationManager();
    }

    @Bean
    public HostApplicationService buildHostApplicationService(EscalationManager escalationManager) {
        return new HostApplicationServiceImpl(escalationManager);
    }

    @Bean
    public AreaInfoService buildAreaInfoService(HostService hostService) {
        return new AreaInfoServiceImpl(hostService);
    }
}
