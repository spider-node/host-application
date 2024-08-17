package cn.spider.framework.code.agent.controller;

import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitParam;
import cn.spider.framework.code.agent.areabase.utils.Wrapper;
import cn.spider.framework.code.agent.data.DeployAreaFunctionParam;
import cn.spider.framework.code.agent.function.FunctionManager;
import cn.spider.framework.code.agent.project.factory.data.CreateProjectResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.spider.framework.code.agent.areabase.utils.WrapMapper;

import javax.annotation.Resource;

@RestController
@RequestMapping("/code_agent")
public class FunctionController {

    @Resource
    private FunctionManager functionManager;

    @PostMapping("/deploy")
    public Wrapper<CreateProjectResult> deployAreaFunction(@RequestBody DeployAreaFunctionParam param) {
        try {
            CreateProjectResult result = functionManager.buildProject(param);
            return WrapMapper.ok(result);
        } catch (Exception e) {
            return WrapMapper.wrap(e);
        }
    }

    @PostMapping("/init_area_base")
    public void initAreaBase(@RequestBody AreaDomainInitParam param) {
        functionManager.initBaseProject(param);
    }
}
