package cn.spider.framework.host.application.base.plugin;

import cn.spider.framework.host.application.base.base.ClientFactoryBean;
import com.alipay.sofa.runtime.api.client.ServiceClient;
import com.alipay.sofa.runtime.api.client.param.ServiceParam;

/**
 * 发布领域功能的能力
 */
public class TaskPublish {
    private ClientFactoryBean clientFactoryBean;

    public TaskPublish(ClientFactoryBean clientFactoryBean) {
        this.clientFactoryBean = clientFactoryBean;
    }

    /**
     * 发布task的额能力-- 当前支持一个业务层面的的能力
     * @param taskService
     */
    public void publish(TaskService taskService,String uniqueId){
        ServiceClient serviceClient=clientFactoryBean.getClientFactory().getClient(ServiceClient.class);
        ServiceParam serviceParam=new ServiceParam();
        serviceParam.setInstance(taskService);
        serviceParam.setUniqueId(uniqueId);
        serviceParam.setInterfaceType(TaskService.class);
        serviceClient.service(serviceParam);
    }
}
