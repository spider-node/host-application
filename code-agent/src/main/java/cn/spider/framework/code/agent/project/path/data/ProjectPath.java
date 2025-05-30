package cn.spider.framework.code.agent.project.path.data;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectPath {

    /**
     * 项目的根路径
     */
    private String projectAreaPath;

    private String projectRootPath;

    /**
     * pom中的groupId
     */
    private String groupId;

    /**
     * pom中的artifactId
     */
    private String artifactId;

    /**
     * pom中的版本
     */
    private String version;

    /**
     * java文件的路径
     */
    private String javaFilePath;

    /**
     * 新项目的pomPath
     */
    private String pomPath;

    /**
     * 启动类的path
     */
    private String mainPath;

    /**
     * service类
     */
    private String startClassPackagePath;

    /**
     * spider的业务方法类path
     */
    private String servicePath;

    /**
     * spider参数的路径path
     */
    private String dataPath;

    /**
     * 其他类的路径path
     */
    private String otherCodePath;

    /**
     * 配置类的路径
     */
    private String propertiesPath;

    /**
     * 业务config类的路径 config主要是spring的配置类增加一些base信息
     */
    private String configPath;

    private String configPackage;

    /**
     * 代码的根路径
     */
    private String codePath;

    /**
     * 跟节点的路径
     */
    private String rootPackagePath;

    /**
     * 实体路径
     */
    private String entityPath;

    /**
     * 生成好的jar包文件路径path
     */
    private String jarFilePath;

    private String jarFileName;

    /**
     * yaml文件路径
     */
    private String yamlPath;

    /**
     * 业务名称
     */
    private String bizName;

    /**
     * 镜像地址
     */
    private String imageUrl;

    /**
     * 命名空间
     */
    private String namespace;

    /**
     * 基础base名称
     */
    private String baseName;

    private String deploymentYamlPath;


}
