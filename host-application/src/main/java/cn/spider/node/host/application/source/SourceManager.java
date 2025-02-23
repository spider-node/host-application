package cn.spider.node.host.application.source;

import cn.spider.node.host.application.source.entity.AreaDatasourceInfo;
import cn.spider.node.host.application.source.service.IAreaDatasourceInfoService;
import cn.spider.node.host.application.util.JdbcUtil;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SourceManager {

    private IAreaDatasourceInfoService datasourceInfoService;

    private Map<String,DataSource> dataSourceMap;

    public SourceManager(IAreaDatasourceInfoService datasourceInfoService) {
        this.datasourceInfoService = datasourceInfoService;
        this.dataSourceMap = new ConcurrentHashMap<>();
    }

    // 初始化数据源mysql
    public void initDataSource() {
        List<AreaDatasourceInfo> areaDatasourceInfos = datasourceInfoService.lambdaQuery().ge(AreaDatasourceInfo :: getId,0).list();
        for (AreaDatasourceInfo datasourceInfo : areaDatasourceInfos) {
            if(dataSourceMap.containsKey(datasourceInfo.getDatasource())){
                continue;
            }
            dataSourceMap.put(JdbcUtil.queryDatabaseName(datasourceInfo.getDatasource()),dataSource(datasourceInfo));
        }
    }
    // 提供获取 mysql数据源的能力

    public DataSource dataSource(AreaDatasourceInfo datasourceInfo) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(datasourceInfo.getUrl());
        dataSource.setUsername(datasourceInfo.getName());
        dataSource.setPassword(datasourceInfo.getPassword());
        dataSource.setDriverClassName(datasourceInfo.getDriverClassName());
        // 配置初始化大小、最小、最大
        dataSource.setInitialSize(datasourceInfo.getInitialSize());
        dataSource.setMinIdle(datasourceInfo.getMinIdle());
        dataSource.setMaxActive(datasourceInfo.getMaxIdle());
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

    public DataSource queryDataSource(String sourceKey){
        return dataSourceMap.get(sourceKey);
    }
}
