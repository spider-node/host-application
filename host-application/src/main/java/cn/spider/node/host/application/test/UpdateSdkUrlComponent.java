package cn.spider.node.host.application.test;

import cn.spider.framework.annotation.TaskComponent;
import cn.spider.framework.annotation.TaskService;
import com.alipay.sofa.runtime.api.annotation.SofaService;
import org.springframework.stereotype.Service;

@Service
@SofaService
@TaskComponent(name = "update_sdk_url")
public class UpdateSdkUrlComponent {

    @TaskService(name = "update_sdk_url", functionName = "更新领域sdkUrl", desc = "根据id更新指定领域对象的sdkUrl")
    public void test(String a){
        System.out.println("--------hello");
    }
}
