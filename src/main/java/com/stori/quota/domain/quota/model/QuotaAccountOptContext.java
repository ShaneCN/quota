package com.stori.quota.domain.quota.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * 额度账户操作上下文
 * 用于额度的初始化、增加、扣减
 */
@Getter
@Setter
@Builder
public class QuotaAccountOptContext {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 业务单据号
     */
    private String bizNo;

}
