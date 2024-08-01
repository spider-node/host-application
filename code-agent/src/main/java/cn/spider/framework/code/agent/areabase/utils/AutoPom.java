package cn.spider.framework.code.agent.areabase.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AutoPom {
    public static void main(String[] args) throws IOException {
        String  mysql_host = "jdbc:mysql://47.109.67.130:3306/spider?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&serverTimezone=Asia/Shanghai";

        String mysql_password = "aB.967426";

        String mysql_user = "spider";
        String packageName = "com.fmc";

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("currentYear", 2023);
        PomInfo pomInfo = new PomInfo();
        pomInfo.setName("aaaa");
        pomInfo.setVersion("1.0.1");
        pomInfo.setArtifactId(packageName);
        pomInfo.setGroupId(mysql_user);
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);
        // 指定模板文件所在的路径
        configuration.setDirectoryForTemplateLoading(new File("src/main/resources/templates"));

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");
        Template template = configuration.getTemplate("pom.xml.ftl");
        Writer out = new FileWriter(Paths.get(System.getProperty("user.dir"))+"/spiderarea/pom.xml");
        try {
            template.process(pomInfo,out);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        out.close();

    }
}
