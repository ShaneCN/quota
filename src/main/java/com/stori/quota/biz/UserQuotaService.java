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

        return quotaAccountService.query(QuotaAccountOptContext
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
        checkParam(userId, amount);

        quotaAccountService.init(QuotaAccountOptContext.builder()
                .userId(userId)
                .amount(new BigDecimal(amount))
                .bizNo(genBizNo())
                .build());

        return quotaAccountService.query(QuotaAccountOptContext.builder()
                .userId(userId)
                .build());
    }

    /**
     * 额度增加
     * 注：这里返回最新额度，而非扣减后的额度。在并发情况下，可能不一致
     *
     * @param userId 用户id
     * @param amount 额度
     * @return 最新额度
     * @throws Exception 变更异常，包括但不限于数据库加锁失败
     */
    public QuotaAccount flowIn(String userId, String amount) throws Exception {
        checkParam(userId, amount);
        quotaAccountService.increse(QuotaAccountOptContext.builder()
                .userId(userId)
                .amount(new BigDecimal(amount))
                .bizNo(genBizNo())
                .build());

        return quotaAccountService.query(QuotaAccountOptContext.builder().userId(userId).build());
    }

    /**
     * 额度扣减
     * 注：这里返回最新额度，而非扣减后的额度。在并发情况下，可能不一致
     *
     * @param userId 用户id
     * @param amount 额度
     * @return 最新额度
     * @throws Exception 变更异常，包括但不限于数据库加锁失败、额度超扣
     */
    public QuotaAccount flowOut(String userId, String amount) throws Exception {
        checkParam(userId, amount);
        quotaAccountService.decrease(QuotaAccountOptContext.builder()
                .userId(userId)
                .amount(new BigDecimal(amount))
                .bizNo(genBizNo())
                .build());

        return quotaAccountService.query(QuotaAccountOptContext.builder().userId(userId).build());
    }

    /**
     * 生成业务单号，当前的处理方式比较简单，使用UUID
     * 后续可以统一在此修改
     *
     * @return 业务单号
     */
    private String genBizNo() {
        return UUID.randomUUID().toString();
    }

    /**
     * 校验入参不为空
     *
     * @param userId 用户id
     * @param amount 金额
     * @throws Exception 参数错误异常
     */
    private void checkParam(String userId, String amount) throws Exception {
        if (StringUtils.isEmpty(userId)) {
            throw new Exception("用户名为空");
        }

        if (StringUtils.isEmpty(amount)) {
            throw new Exception("金额为空");
        }

    }
}
