package ${startPath};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan(basePackages = {<#list mapperPath as item>
                                "${item}",
                            </#list>},
sqlSessionTemplateRef = "sqlSessionTemplate")
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class ${startClassName}{
    public static void main(String[] args) {
    SpringApplication application = new SpringApplication(${startClassName}.class);
    application.setWebApplicationType(WebApplicationType.NONE);
    application.run(args);
    }
}