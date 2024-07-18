# quota
额度管理

需求分析、设计文档：https://www.yuque.com/g/imliush/rdz5x4/hwosn840g7p1g36v/collaborator/join?token=2UczxERouEs1t7IU&source=doc_collaborator# 《额度管理-系分》

测试文档：https://www.yuque.com/g/imliush/rdz5x4/ramn0nfs50cf02n2/collaborator/join?token=oFKHMABR3Xuv011L&source=doc_collaborator# 《自测》


定时任务代码入口：com.stori.quota.api.ScheduleTask

额度管理代码入口：com.stori.quota.api.QuotaController

---
因为时间比赶，部分细节没有来得及处理：


    - 业务类异常和技术类异常分别继承Exception类，方便各层catch不同类型异常；
    - 定义异常类型枚举，方便上层catch后，根据不同枚举类型执行相应的操作；
    - 流水bizNo使用了uuid，没有记录业务信息
    - 没有设计用于前端展示的vo，所以不好处理返回前端的时间
    - 各层没有规范的日志
    - 定时任务触发的任务可以用
    - 记录更新日期字段可以用拦截器来做
    - 账户操作大量重复逻辑，可以用模板方法简化
