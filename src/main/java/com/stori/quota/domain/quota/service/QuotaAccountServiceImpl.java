package com.stori.quota.domain.quota.service;

import com.stori.quota.domain.quota.model.QuotaAccount;
import com.stori.quota.domain.quota.model.QuotaAccountOptContext;
import com.stori.quota.domain.quota.repo.QuotaAccountRepo;
import com.stori.quota.infras.dao.entity.UserAccountDO;
import com.stori.quota.infras.dao.mapper.UserAccountDOMapper;
import com.stori.quota.infras.enums.OptTypeEnum;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 额度账户实现
 */
@Component
public class QuotaAccountServiceImpl implements QuotaAccountService {


    /** 额度账户仓储层 */
    @Resource
    private QuotaAccountRepo quotaAccountRepo;

    /** 事务模板 */
    @Resource
    private TransactionTemplate transactionTemplate;

    /** 用户账户DAO */
    @Resource
    private UserAccountDOMapper userAccountDOMapper;

    /**
     * @see QuotaAccountService#query(QuotaAccountOptContext)
     */
    @Override
    public QuotaAccount query(QuotaAccountOptContext context) throws Exception {
        return queryWithThrow(context);
    }

    /**
     * @see QuotaAccountService#init(QuotaAccountOptContext)
     */
    @Override
    public void init(QuotaAccountOptContext context) throws Exception {
        String userId = context.getUserId();
        BigDecimal amount = context.getAmount();

        // 1. 查询，判断重复
        QuotaAccount queryAccount = quotaAccountRepo.queryByUserId(userId);
        if (Objects.nonNull(queryAccount)) {
            throw new Exception("重复创建账户");
        }

        // 2. 初始化对象
        QuotaAccount newAccount = QuotaAccount.init(userId, amount);

        // 3. 保存
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    quotaAccountRepo.store(newAccount, context.getBizNo());
                } catch (Exception e) {
                    throw new RuntimeException("账户数据插入失败", e);
                }
            }
        });
    }

    /**
     * @see QuotaAccountService#increse(QuotaAccountOptContext)
     */
    @Override
    public void increse(QuotaAccountOptContext context) throws Exception {

        // 1.校验账户是否存在
        QuotaAccount queryAccount = queryWithThrow(context);

        // 2. 额度记增
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    doIncreaseWithThrow(context);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * @see QuotaAccountService#decrease(QuotaAccountOptContext)
     */
    @Override
    public void decrease(QuotaAccountOptContext context) throws Exception {

        // 1.校验账户是否存在
        QuotaAccount queryAccount = queryWithThrow(context);

        // 2.额度记减
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    doDecreaseWithThrow(context);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * 根据用户id，查询账户信息，为空时抛异常
     *
     * @param context 查询上下文
     * @return 账号信息
     * @throws Exception 账户不存在时，抛出异常
     */
    private QuotaAccount queryWithThrow(QuotaAccountOptContext context) throws Exception {
        QuotaAccount queryAccount = quotaAccountRepo.queryByUserId(context.getUserId());
        if (Objects.isNull(queryAccount)) {
            throw new Exception("账户不存在");
        }
        return queryAccount;
    }

    /**
     * 执行扣减
     *
     * @param context 上下文，包含用户id、金额
     * @throws Exception 账户不存在、加锁失败、更新失败，抛出异常
     */
    private void doIncreaseWithThrow(QuotaAccountOptContext context) throws Exception {
        // 1. 加锁
        UserAccountDO userAccountDO = userAccountDOMapper.lockByUserIdNoWait(context.getUserId());

        // 2. 判断数据是否合法
        QuotaAccount queryQuotaAccount = QuotaAccount.fromDO(userAccountDO);
        if (Objects.isNull(queryQuotaAccount)) {
            throw new Exception("账户不存在");
        }

        // 3. 更新数据
        queryQuotaAccount.increase(context.getAmount());
        quotaAccountRepo.update(queryQuotaAccount, context, OptTypeEnum.INCRE);
    }

    /**
     * 执行增加额度
     *
     * @param context 上下文，包含用户id、金额、业务号
     * @throws Exception 账户不存在、加锁失败、更新失败，抛出异常
     */
    private void doDecreaseWithThrow(QuotaAccountOptContext context) throws Exception {
        // 1. 加锁
        UserAccountDO userAccountDO = userAccountDOMapper.lockByUserIdNoWait(context.getUserId());

        // 2. 判断数据是否合法
        QuotaAccount queryQuotaAccount = QuotaAccount.fromDO(userAccountDO);
        if (Objects.isNull(queryQuotaAccount)) {
            throw new Exception("账户不存在");
        }

        // 3. 更新数据
        queryQuotaAccount.decrese(context.getAmount());
        quotaAccountRepo.update(queryQuotaAccount, context, OptTypeEnum.DECRE);
    }
}
