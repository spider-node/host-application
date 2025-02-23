package cn.spider.framework.code.agent.config;

import cn.spider.framework.code.agent.function.AreaProjectNode;
import cn.spider.framework.code.agent.spider.SpiderClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@ComponentScan(basePackages = {"cn.spider.framework.code.agent.*"})
public class CodeAgentConfig {

    @Bean
    public OkHttpClient buildOkHttpClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    public SpiderClient buildSpiderClient(
                                          @Value("${spider.server.requestFileUrl:}") String requestFileUrl,
                                          @Value("${spider.server.deployUrl:}") String deployUrl,
                                          @Value("${spider.server.ip:}") String spiderIp,
                                          @Value("${spider.server.port:}")  String spiderPort,
                                          OkHttpClient client){
        String spiderServerIp = System.getenv(Constant.SPIDER_SERVER_IP);
        if(StringUtils.isEmpty(spiderServerIp)){
            spiderServerIp = spiderIp;
        }
        String spiderServerPort = System.getenv(Constant.SPIDER_SERVER_PORT);
        if(StringUtils.isEmpty(spiderServerPort)){
            spiderServerPort = spiderPort;
        }
        log.info("配置的 spiderServerIp{}, spiderServerPort{}", spiderServerIp, spiderServerPort);
        return new SpiderClient(spiderServerIp,Integer.parseInt(spiderServerPort),requestFileUrl,client,deployUrl);
    }

    @Bean
    public AreaProjectNode buildAreaProjectNode(){
        return new AreaProjectNode();
    }

    @Bean
    public BaseDeployConfig buildBaseDeployConfig(@Value("${spider.default.namespace:}") String defaultNamespace,@Value("${spider.application.base.name:}") String baseName){
        String defaultNamespaceNew = System.getenv(Constant.DEFAULT_NAMESPACE);
        if(StringUtils.isEmpty(defaultNamespaceNew)){
            defaultNamespaceNew = defaultNamespace;
        }
        String baseNameNew = System.getenv(Constant.BASE_NAME);
        if(StringUtils.isEmpty(baseNameNew)){
            baseNameNew = baseName;
        }
        return new BaseDeployConfig(defaultNamespaceNew, baseNameNew);
    }

}
