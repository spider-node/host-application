<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

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
        <!-- Example dependency -->
        <dependency>
            <groupId>cn.spider-node</groupId>
            <artifactId>host-application-base</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!--这里添加ark 打包插件-->
            <plugin>
                <groupId>com.alipay.sofa</groupId>
                <artifactId>sofa-ark-maven-plugin</artifactId>
                <version>${sofa.ark.version}</version>
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
                    <bizName>${artifactId}</bizName>
                    <webContextPath>provider</webContextPath>
                    <declaredMode>true</declaredMode>
                    <packExcludesConfig>rules.txt</packExcludesConfig>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>