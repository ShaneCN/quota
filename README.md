# quota
本工程是额度管理

因为时间比赶，部分细节没有来得及处理：


    - 业务类异常和技术类异常分别继承Exception类，方便各层catch不同类型异常；
    - 定义异常类型枚举，方便上层catch后，根据不同枚举类型执行相应的操作；
    - 流水bizNo使用了uuid，没有记录业务信息
    - 没有设计用于前端展示的vo，所以不好处理返回前端的时间
    - 各层没有规范的日志
    - 记录更新日期字段可以用拦截器来做
