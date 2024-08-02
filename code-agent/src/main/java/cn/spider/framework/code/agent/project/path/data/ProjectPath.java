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


}
