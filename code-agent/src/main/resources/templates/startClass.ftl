package ${startPath};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = {"${mapperPath}"},
sqlSessionTemplateRef = "sqlSessionTemplate")
public class ${startClassName}{
    public static void main(String[] args) {
    SpringApplication.run(${startClassName}.class, args);
    }
}