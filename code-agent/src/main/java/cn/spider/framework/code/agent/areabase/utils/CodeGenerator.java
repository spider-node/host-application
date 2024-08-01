package cn.spider.framework.code.agent.areabase.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;

public class CodeGenerator {
    public static void main(String[] args) {
        String  mysql_host = "jdbc:mysql://47.109.67.130:3306/spider?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai";

        String mysql_password = "aB.967426";

        String mysql_user = "spider";
        String packageName = "cn.spider.areabase.modules";
        FastAutoGenerator.create(mysql_host, mysql_user, mysql_password)
                .globalConfig(builder -> builder
                        .author("ATu")
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
                        //.outputDir(Paths.get(System.getProperty("user.dir")) + "/spiderarea/src/main/java")
                        .commentDate("yyyy-MM-dd")

                )
                .packageConfig(builder -> builder
                        .parent(packageName)
                        .moduleName("domaininfo")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                        .addInclude("area_domain_info")
                        .entityBuilder()
                        .enableLombok()
                        .enableTableFieldAnnotation() // 启用字段注解
                        .controllerBuilder()
                        .serviceBuilder().formatServiceFileName("%sService").build()

                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
