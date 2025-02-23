package cn.spider.framework.code.agent.util;

import java.util.Map;

public class K8sDeployUtil {
    public static String buildDeployYaml(Map<String, Object> dataModel) {
        return "apiVersion: apps/v1\n" +
                "kind: Deployment\n" +
                "metadata:\n" +
                "  name: " + dataModel.get("bizName") + "\n" +
                "  namespace: " + dataModel.get("namespace") + "\n" +
                "  labels:\n" +
                "    virtual-kubelet.koupleless.io/component: module-deployment\n" +
                "spec:\n" +
                "  replicas: 1\n" +
                "  selector:\n" +
                "    matchLabels:\n" +
                "      module: " + dataModel.get("bizName") + "\n" +
                "  template:\n" +
                "    metadata:\n" +
                "      labels:\n" +
                "        module: " + dataModel.get("bizName") + "\n" +
                "        virtual-kubelet.koupleless.io/component: module\n" +
                "    spec:\n" +
                "      containers:\n" +
                "        - name: "  + dataModel.get("bizName") + "\n" +
                "          image: " + dataModel.get("imageUrl") + "\n" +
                "          env:\n" +
                "            - name: BIZ_VERSION\n" +
                "              value: " + dataModel.get("bizVersion") + "\n" +
                "      affinity:\n" +
                "        nodeAffinity:\n" +
                "          requiredDuringSchedulingIgnoredDuringExecution:\n" +
                "            nodeSelectorTerms:\n" +
                "              - matchExpressions:\n" +
                "                  # these labels in vnode generated in base `https://github.com/koupleless/runtime/blob/main/arklet-core/src/main/java/com/alipay/sofa/koupleless/arklet/core/hook/base/BaseMetadataHookImpl.java`\n" +
                "                  # you can define your own labels by implementing your own BaseMetadataHookImpl\n" +
                "                  - key: base.koupleless.io/name\n" +
                "                    operator: In\n" +
                "                    values:\n" +
                "                      - TO_BE_IMPLEMENTED\n" +
                "                  - key: base.koupleless.io/cluster-name\n" +
                "                    operator: In\n" +
                "                    values:\n" +
                "                      - default\n" +
                "      tolerations:\n" +
                "        - key: \"schedule.koupleless.io/virtual-node\"\n" +
                "          operator: \"Equal\"\n" +
                "          value: \"True\"\n" +
                "          effect: \"NoExecute\"\n" +
                "        - key: \"schedule.koupleless.io/node-env\"\n" +
                "          operator: \"Equal\"\n" +
                "          value: \"dev\"\n" +
                "          effect: \"NoExecute\"\n";
    }
}
