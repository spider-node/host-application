package cn.spider.node.host.application;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@MapperScan(basePackages = {"cn.spider.*.host.application.source.mapper"},
		sqlSessionTemplateRef = "sqlSessionTemplate")
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class })
public class HostApplication {
	public static void main(String[] args) {
		SpringApplication.run(HostApplication.class, args);
	}

}
