package cn.spider.framework.code.agent.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class FltlUtil {

    /**
     *
     * @param outputPath 输出的路径,文件存在会进行覆盖
     * @param dataModel  传入的模板的参数
     * @param templateFtl 模板
     * @throws IOException io的一场
     * @throws TemplateException 模板一场
     */
    public static void generateFile(String outputPath,Map<String, Object> dataModel,String templateFtl) throws IOException, TemplateException {
        // 创建一个配置实例
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        // 设置模板路径
        cfg.setClassForTemplateLoading(FltlUtil.class, "/templates");
        // 获取模板
        Template template = cfg.getTemplate(templateFtl);
        // 渲染模板到文件
        File pomFile = new File(outputPath);
        try (FileWriter fileWriter = new FileWriter(pomFile)) { // 使用FileWriter自动覆盖已存在的文件
            template.process(dataModel, fileWriter);
        }
        log.info("Generated file path at: {}",outputPath);
    }

}
