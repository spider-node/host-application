package cn.spider.framework.code.agent.areabase.utils;

import cn.hutool.core.io.file.FileReader;
import cn.spider.framework.code.agent.areabase.modules.datasourceinfo.entity.AreaDatasourceInfo;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInfo;
import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitParam;
import cn.spider.framework.code.agent.util.ClassUtil;
import cn.spider.framework.code.agent.util.FieldInfo;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CodeGenerator3 {

    public static void initCode(AreaDatasourceInfo datasourceInfo, AreaDomainInitParam areaInitReq, String outPath) {
        FastAutoGenerator.create(datasourceInfo.getUrl(), datasourceInfo.getName(), datasourceInfo.getPassword())
                .globalConfig(builder -> builder
                        .author("dds")
                        .outputDir(outPath)
                        .commentDate("yyyy-MM-dd")

                )
                .packageConfig(builder -> builder
                        .parent(areaInitReq.getPackageName())
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .pathInfo(Collections.singletonMap(OutputFile.controller, null))
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                        .addInclude(areaInitReq.getTableName())
                        .entityBuilder()
                        .enableLombok()
                        .enableTableFieldAnnotation() // 启用字段注解
                        .controllerBuilder()
                        .serviceBuilder()
                        .formatServiceFileName("%sService")
                        .controllerBuilder().disable()
                        .build()

                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();

    }

    private static String baseJavaPath(String tableName){
        ArrayList<String> pathArray = new ArrayList<>();
        pathArray.add(tableName);
        pathArray.add("src");
        pathArray.add("main");
        pathArray.add("java");
        String[] a = pathArray.toArray(new String[0]);

        return Paths.get(System.getProperty("user.dir"), Arrays.copyOfRange(a, 0, a.length)).toString();
    }

    private static String basePomPath(String tableName){
        ArrayList<String> pathArray = new ArrayList<>();
        pathArray.add(tableName);
        pathArray.add("pom.xml");
        String[] a = pathArray.toArray(new String[0]);
        return Paths.get(System.getProperty("user.dir"), Arrays.copyOfRange(a, 0, a.length)).toString();
    }

    public static AreaDomainInfo initAreaDomainInfo(AreaDatasourceInfo datasourceInfo, AreaDomainInitParam areaInitReq,String outPath){
        //生成后就获取entity内容1.找到entity的path
        String[] packageStr = areaInitReq.getPackageName().split("\\.");
        List<String> packageList = Arrays.stream(packageStr).collect(Collectors.toList());
        packageList.add("entity");
        String objectTableName= StringUtils.convertToCamelCase(areaInitReq.getTableName());
        packageList.add(objectTableName+".java");
        String[] a1 = packageList.toArray(new String[0]);
        String entityPath = Paths.get(outPath,Arrays.copyOfRange(a1, 0, a1.length)).toString();

        FileReader fileReader = new FileReader(entityPath);
        String entityStr = fileReader.readString();

        //组装AreaDomainInfo
        AreaDomainInfo areaDomainInfo = new AreaDomainInfo();
        areaDomainInfo.setTableName(areaInitReq.getTableName());
        areaDomainInfo.setDatasourceId(datasourceInfo.getId());
        areaDomainInfo.setDomainObject(JSON.toJSONString(ClassUtil.parseFieldInfo(entityStr)));
        // package cn.fmc.aax.entity;
        areaDomainInfo.setDomainObjectPackage("package "+areaInitReq.getPackageName()+".entity");
        areaDomainInfo.setDomainObjectEntityName(objectTableName);
        areaDomainInfo.setDomainObjectServiceName(objectTableName+"Service");
        areaDomainInfo.setDomainObjectServicePackage("package "+areaInitReq.getPackageName()+".service");
        areaDomainInfo.setDomainObjectServiceImplName(objectTableName+"ServiceImpl");
        //package cn.fmc.aax.service.impl;
        areaDomainInfo.setDomainObjectServiceImplPackage("package "+areaInitReq.getPackageName()+".service.impl");
        return areaDomainInfo;
    }
}
