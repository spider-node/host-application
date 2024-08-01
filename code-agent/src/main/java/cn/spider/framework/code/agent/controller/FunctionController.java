package cn.spider.framework.code.agent.controller;

import cn.spider.framework.code.agent.data.DeployAreaFunctionParam;
import cn.spider.framework.code.agent.function.FunctionManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/code_agent")
public class FunctionController {

    @Resource
    private FunctionManager functionManager;

    @PostMapping("/deploy")
    public void deployAreaFunction(@RequestBody DeployAreaFunctionParam param){
        functionManager.buildProject(param);
    }
}
