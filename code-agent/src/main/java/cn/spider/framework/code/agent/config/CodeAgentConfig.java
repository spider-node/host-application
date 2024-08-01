package cn.spider.framework.code.agent.config;

import cn.spider.framework.code.agent.function.AreaProjectNode;
import cn.spider.framework.code.agent.spider.SpiderClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CodeAgentConfig {

    @Bean
    public OkHttpClient buildOkHttpClient(){
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Bean
    public SpiderClient buildSpiderClient(@Value("${spider.server.ip}") String spiderHost, @Value("${spider.server.port}") String spiderPort, @Value("${spider.server.requestFileUrl}") String requestFileUrl, @Value("${spider.server.registerAreaFunctionUrl}") String registerAreaFunctionUrl, OkHttpClient client){

        System.out.println(spiderPort);
        return new SpiderClient(spiderHost,Integer.parseInt(spiderPort),requestFileUrl,registerAreaFunctionUrl,client);
    }

    @Bean
    public AreaProjectNode buildAreaProjectNode(){
        return new AreaProjectNode();
    }


}
