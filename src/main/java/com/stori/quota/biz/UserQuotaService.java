package com.stori.quota.biz;

import com.stori.quota.domain.quota.model.QuotaAccount;
import com.stori.quota.domain.quota.model.QuotaAccountOptContext;
import com.stori.quota.domain.quota.service.QuotaAccountService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * 用户额度账户业务层
 * 主要校验请求参数
 */
@Component
public class UserQuotaService {

    /** 额度账户域 */
    @Resource
    private QuotaAccountService quotaAccountService;

    /**
     * 查询额度账户
     *
     * @param userId 用户id
     * @return 账户
     * @throws Exception 异常
     */
    public QuotaAccount query(String userId) throws Exception {

        if (StringUtils.isEmpty(userId)) {
            throw new Exception("入参为空");
        }

        return quotaAccountService.query(
                QuotaAccountOptContext
                        .builder()
                        .userId(userId)
                        .build());
    }

    /**
     * 开通额度账户
     *
     * @param userId 用户id
     * @param amount 金额
     * @return 账户
     * @throws Exception 异常
     */
    public QuotaAccount openAccount(String userId, String amount) throws Exception {

        if (StringUtils.isEmpty(userId)) {
            throw new Exception("用户名为空");
        }

        if (StringUtils.isEmpty(amount)) {
            throw new Exception("金额为空");
        }

        quotaAccountService.init(QuotaAccountOptContext.builder()
                .userId(userId)
                .amount(new BigDecimal(amount))
                .bizNo(genBizNo())
                .build());

        return quotaAccountService.query(QuotaAccountOptContext.builder().
                userId(userId)
                .build());
    }

    public QuotaAccount flowIn(String userId, String amount) throws Exception {
        quotaAccountService.increse(QuotaAccountOptContext.builder()
                .userId(userId)
                .amount(new BigDecimal(amount))
                .bizNo(genBizNo())
                .build());

        return quotaAccountService.query(QuotaAccountOptContext.builder()
                .userId(userId)
                .build());
    }

    public QuotaAccount flowOut(String userId, String amount) throws Exception {
        quotaAccountService.decrease(QuotaAccountOptContext.builder()
                .userId(userId)
                .amount(new BigDecimal(amount))
                .bizNo(genBizNo())
                .build());

        return quotaAccountService.query(QuotaAccountOptContext.builder()
                .userId(userId)
                .build());
    }

    private String genBizNo() {
        return UUID.randomUUID().toString();
    }
}
