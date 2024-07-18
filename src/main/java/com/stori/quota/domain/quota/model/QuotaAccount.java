package com.stori.quota.domain.quota.model;

import com.stori.quota.infras.dao.entity.UserAccountDO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * 额度账户
 */
@Getter
@Setter
public class QuotaAccount implements Serializable {

    /** 账户id */
    private Long id;

    /** 用户id */
    private String userId;

    /** 额度 */
    private BigDecimal amount;

    /** 账户创建时间 */
    private Date gmtCreate;

    /** 账户最后一次修改时间 */
    private Date gmtModified;

    /**
     * 通过用户名和金额，初始化账户
     *
     * @param userId 用户id
     * @param amount 金额
     * @return 初始化后的账户
     */
    public static QuotaAccount init(String userId, BigDecimal amount) {
        QuotaAccount account = new QuotaAccount();
        account.setUserId(userId);
        account.setAmount(amount);
        account.setGmtCreate(new Date());
        account.setGmtModified(new Date());
        return account;
    }

    /**
     * 增加账户额度
     *
     * @param modiAmount 增加额度
     */
    public void increase(BigDecimal modiAmount) {
        this.amount = this.amount.add(modiAmount);
        this.gmtModified = new Date();
    }

    /**
     * 扣减账户额度
     *
     * @param modiAmount 扣减额度
     * @throws Exception 超支时抛异常
     */
    public void decrese(BigDecimal modiAmount) throws Exception {
        if (this.amount.compareTo(modiAmount) < 0) {
            throw new Exception("额度不足");
        }

        this.amount = this.amount.subtract(modiAmount);
        this.gmtModified = new Date();
    }

    public static QuotaAccount fromDO(UserAccountDO userAccountDO) {
        if (Objects.isNull(userAccountDO)) {
            return null;
        }
        QuotaAccount quotaAccount = new QuotaAccount();
        quotaAccount.setId(userAccountDO.getId());
        quotaAccount.setUserId(userAccountDO.getUserId());
        quotaAccount.setAmount(userAccountDO.getAmount());
        quotaAccount.setGmtCreate(userAccountDO.getGmtCreate());
        quotaAccount.setGmtModified(userAccountDO.getGmtModified());

        return quotaAccount;
    }

    public static UserAccountDO toDO(QuotaAccount quotaAccount) {
        if (Objects.isNull(quotaAccount)) {
            return null;
        }
        return UserAccountDO.builder()
                .id(quotaAccount.getId())
                .userId(quotaAccount.getUserId())
                .amount(quotaAccount.getAmount())
                .gmtCreate(quotaAccount.getGmtCreate())
                .gmtModified(quotaAccount.getGmtModified())
                .build();
    }

}
