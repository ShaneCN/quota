package com.stori.quota.domain.quota.repo;

import com.stori.quota.domain.quota.model.QuotaAccount;
import com.stori.quota.domain.quota.model.QuotaAccountOptContext;
import com.stori.quota.infras.enums.OptTypeEnum;

public interface QuotaAccountRepo {

    /**
     * 根据用户id查询额度账户
     *
     * @param userId 用户id
     * @return 额度账户
     */
    QuotaAccount queryByUserId(String userId);

    /**
     * 根据用户id加锁
     * 锁冲突立即返回
     *
     * @param userId 用户id
     * @return 额度账户
     */
    QuotaAccount lockByUserId(String userId);

    /**
     * 插入新的额度账户
     *
     * @param account 额度账户
     */
    void store(QuotaAccount account, String bizNo) throws Exception;

    /**
     * 更新额度账户
     *
     * @param account 额度账户
     */
    void update(QuotaAccount account, QuotaAccountOptContext context, OptTypeEnum optType) throws Exception;



}
