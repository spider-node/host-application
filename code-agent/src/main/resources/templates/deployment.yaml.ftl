apiVersion: apps/v1  # 指定api版本，此值必须在kubectl api-versions中
kind: Deployment  # 指定创建资源的角色/类型
metadata:  # 资源的元数据/属性
  name: ${bizName}  # 资源的名字，在同一个namespace中必须唯一
  namespace: ${namespace} # 部署在哪个namespace中
spec: # 资源规范字段
  replicas: 2
  revisionHistoryLimit: 3 # 保留历史版本
  selector: # 选择器
    matchLabels: # 匹配标签
      app: ${bizName}
  strategy: # 策略
    rollingUpdate: # 滚动更新
      maxSurge: 30% # 最大额外可以存在的副本数，可以为百分比，也可以为整数
      maxUnavailable: 30% # 示在更新过程中能够进入不可用状态的 Pod 的最大值，可以为百分比，也可以为整数
    type: RollingUpdate # 滚动更新策略
  template: # 模版
    metadata: # 资源的元数据/属性
      labels: # 设定资源的标签
        module-controller.koupleless.io/component: module # 必要，声明pod的类型，用于module controller管理
        # deployment unique id
        app: ${bizName}-non-peer
    spec: # 资源规范字段
      containers:
        - name: ${artifactId} # 必要，声明module的bizName，需与pom中声明的artifactId保持一致
          image: ${imageUrl}
          env:
            - name: BIZ_VERSION # 必要，声明module的biz_version，value需与pom中声明的version保持一致
              value: ${bizVersion}
      affinity:
        nodeAffinity: # 必要，声明基座选择器，保证模块被调度到指定的基座上
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: base.koupleless.io/cluster-name
                    operator: In
                    values:
                      - default
                  - key: base.koupleless.io/name
                    operator: In
                    values:
                      - ${baseName}  # 指定的基座bizName，必填，至少需要一个
      tolerations: # 必要，允许pod被调度到基座node上
        - key: "schedule.koupleless.io/virtual-node"
          operator: "Equal"
          value: "True"
          effect: "NoExecute"