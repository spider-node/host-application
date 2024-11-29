package cn.spider.framework.code.agent.controller;

import cn.spider.framework.code.agent.areabase.modules.domaininfo.entity.AreaDomainInitParam;
import cn.spider.framework.code.agent.areabase.utils.Wrapper;
import cn.spider.framework.code.agent.data.DeployAreaFunctionParam;
import cn.spider.framework.code.agent.function.AreaProjectNode;
import cn.spider.framework.code.agent.function.FunctionManager;
import cn.spider.framework.code.agent.project.factory.data.InitAreaBaseResult;
import cn.spider.framework.code.agent.spider.SpiderClient;
import cn.spider.node.framework.code.agent.sdk.data.CreateProjectResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.spider.framework.code.agent.areabase.utils.WrapMapper;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@RestController
@RequestMapping("/code_agent")
public class FunctionController {

    @Resource
    private FunctionManager functionManager;

    @PostMapping("/build_area_plugin")
    public Wrapper<CreateProjectResult> deployAreaFunction(@RequestBody DeployAreaFunctionParam param) {
        try {
            log.info("请求的参数为-deployAreaFunction {}", JSON.toJSONString(param));
            CreateProjectResult result = functionManager.buildProject(param);
            return WrapMapper.ok(result);
        } catch (Exception e) {
            return WrapMapper.wrap(e);
        }
    }

    @PostMapping("/init_area_base")
    public Wrapper<InitAreaBaseResult> initAreaBase(@RequestBody AreaDomainInitParam param) {
        return WrapMapper.ok(functionManager.initBaseProject(param));
    }
    @Resource
    private AreaProjectNode areaProjectNode;

    @Resource
    private SpiderClient spiderClient;
    // 测试读取文件,进行上传
    @PostMapping("/test_upload_file")
    public Wrapper<String>  test(@RequestBody JSONObject param) throws IOException {
        Path jar = areaProjectNode.readJarFilesInDirectory(param.getString("path"));
        String url = spiderClient.uploadFile(jar);
        log.info("上传的路径为 {}",url);
        return WrapMapper.ok(url);
    }


}
