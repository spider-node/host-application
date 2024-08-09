package cn.spider.node.host.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = {"cn.spider.*.host.application.source.mapper"},
		sqlSessionTemplateRef = "sqlSessionTemplate")
public class HostApplication {
	public static void main(String[] args) {
		SpringApplication.run(HostApplication.class, args);
	}

}
