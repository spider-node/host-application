package cn.spider.framework.code.agent.project.factory.data;
import cn.spider.framework.code.agent.project.factory.data.enums.CreateProjectStatus;
import lombok.Data;

@Data
public class CreateProjectResult {
    /**
     * 部署code
     */
    private CreateProjectStatus status;

    /**
     * 部署过程异常
     */
    private String errorStackTrace;
}
