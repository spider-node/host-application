<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.alipay.sofa</groupId>
        <artifactId>sofaboot-dependencies</artifactId>
        <version>3.21.0</version>
        <!-- lookup parent from repository -->
    </parent>

    <groupId>${groupId}</groupId>
    <artifactId>${artifactId}</artifactId>
    <version>${version}</version>
    <packaging>jar</packaging>

    <name>${projectName}</name>
    <description>${projectDescription}</description>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <sofa.ark.version>2.2.12</sofa.ark.version>
    </properties>

    <dependencies>
        <!-- 功能插件的基础包 -->
        <dependency>
            <groupId>cn.spider-node</groupId>
            <artifactId>host-application-base</artifactId>
            <version>1.0.0</version>
            <scope>provided</scope>
        </dependency>

        <!-- 归属问题 -->
        <#list basePoms as item>

            <dependency>
                <groupId>${item.groupId}</groupId>
                <artifactId>${item.artifactId}</artifactId>
                <version>${item.version}</version>
            </dependency>
        </#list>


        <#if mavenPom??>
            ${mavenPom}
        </#if>


    </dependencies>

    <build>
        <plugins>
            <!--这里添加ark 打包插件-->
            <plugin>
                <groupId>com.alipay.sofa</groupId>
                <artifactId>sofa-ark-maven-plugin</artifactId>
                <version>2.2.12</version>
                <executions>
                    <execution>
                        <id>default-cli</id>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <skipArkExecutable>true</skipArkExecutable>
                    <outputDirectory>./target</outputDirectory>
                    <bizName>${bizName}</bizName>
                    <webContextPath>${bizName}</webContextPath>
                    <declaredMode>true</declaredMode>
                    <packExcludesConfig>rules.txt</packExcludesConfig>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>