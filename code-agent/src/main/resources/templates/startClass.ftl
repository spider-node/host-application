package ${startPath};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@MapperScan(basePackages = {"${mapperPath}"},
sqlSessionTemplateRef = "sqlSessionTemplate")
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
public class ${startClassName}{
    public static void main(String[] args) {
    SpringApplication.run(${startClassName}.class, args);
    }
}