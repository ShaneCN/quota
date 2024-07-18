package com.stori.quota.domain.quota.service;


import com.stori.quota.domain.quota.model.QuotaAccount;
import com.stori.quota.domain.quota.model.QuotaAccountOptContext;

/**
 * 额度账户服务
 * 注：由上游保证传入参数不为null
 */
public interface QuotaAccountService {

    /**
     * 查询账户
     *
     * @param context 查询上下文
     * @return 额度账户
     */
    QuotaAccount query(QuotaAccountOptContext context) throws Exception;

    /**
     * 初始化用户额度账户
     * 初始化时若已存在该账户，抛异常
     *
     * @param context 包含用户id, 初始化额度
     */
    void init(QuotaAccountOptContext context) throws Exception;

    /**
     * 增加账户额度
     *
     * @param context 包含用户id, 增加额度
     */
    void increse(QuotaAccountOptContext context) throws Exception;

    /**
     * 扣减账户额度
     * 如果账户扣减后小于0，增扣减失败抛出异常
     *
     * @param context 包含用户id, 扣减额度
     */
    void decrease(QuotaAccountOptContext context) throws Exception;

}
