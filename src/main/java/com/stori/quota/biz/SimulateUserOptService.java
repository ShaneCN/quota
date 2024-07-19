package com.stori.quota.biz;

import com.stori.quota.domain.quota.model.QuotaAccount;
import com.stori.quota.domain.quota.model.QuotaAccountOptContext;
import com.stori.quota.domain.quota.service.QuotaAccountService;
import com.stori.quota.infras.dao.entity.UserAccountDO;
import com.stori.quota.infras.dao.mapper.UserAccountDOMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 模拟用户操作业务层服务
 */
@Component
@Slf4j
public class SimulateUserOptService {

    /** 随机金额边界，0-99 */
    private final static int AMOUNT_BOUND = 100;

    /** 线程池 */
    private final ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(20, 20, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));

    /** 额度账户服务 */
    @Resource
    private QuotaAccountService quotaAccountService;

    /** 用户账户mapper */
    @Resource
    private UserAccountDOMapper mapper;

    /**
     * 随机操作，有两种方式：
     * 1、对所有用户进行随机操作
     * 2、对单个用户，并发进行随机操作
     */
    public void randomOpt() {

        // 1. 查询所有用户
        List<UserAccountDO> userAccountDOS = mapper.selectAllUsers();
        if (CollectionUtils.isEmpty(userAccountDOS)) {
            log.info("无用户");
            return ;
        }

        // 2. 获取用户id
        List<String> userIds = userAccountDOS.stream().map(UserAccountDO::getUserId).distinct().collect(Collectors.toList());

        // 3. 对列表内的所有用户，执行随机增减金额操作
        multiThreadOpt(userIds);

        // 4. 对单个用户并发增加操作
        multiThreadSingleUserOpt(userIds.get(0));
    }

    /**
     * 并发更新多账户
     *
     * @param userIds 用户id列表
     */
    private void multiThreadOpt(List<String> userIds) {
        for (String userId : userIds) {
            threadPoolExecutor.execute(() -> singleUserRandomOpt(userId));
        }
    }


    /**
     * 多线程操作单用户账户
     * 用于校验并发环境下锁是否生效
     *
     * @param userId 用户id
     */
    private void multiThreadSingleUserOpt(String userId) {
        for (int i = 0; i < 20; i++) {
            threadPoolExecutor.execute(() -> singleUserRandomOpt(userId));
        }
    }


    /**
     * 操作单个用户账户，触发额度增加、扣减的0种，1种或者2种
     * 随机金额 < 100
     *
     * @param userId 用户id
     */
    private void singleUserRandomOpt(String userId) {
        Random random = new Random();
        QuotaAccount before;

        // 1. 如果随机数为奇数，触发增
        if (random.nextInt() % 2 == 1) {
            try {
                before = quotaAccountService.query(QuotaAccountOptContext.builder().userId(userId).build());
                int modified = random.nextInt(AMOUNT_BOUND);
                quotaAccountService.increse(QuotaAccountOptContext.builder()
                        .userId(userId)
                        .amount(BigDecimal.valueOf(modified))
                        .bizNo(UUID.randomUUID().toString())
                        .build());

                log.info("{}记增成功: 变更前金额:{}, 变更金额:{}", userId, before.getAmount(), modified);
            } catch (Exception e) {
                log.error("余额记增失败，userId:{}, 失败原因：{}", userId, e.getMessage());
            }
        }

        // 2. 如果随机数为奇数，触发扣减
        if (random.nextInt() % 2 == 1) {
            try {
                before = quotaAccountService.query(QuotaAccountOptContext.builder().userId(userId).build());
                int modified = random.nextInt(AMOUNT_BOUND);

                quotaAccountService.decrease(QuotaAccountOptContext.builder()
                        .userId(userId)
                        .amount(BigDecimal.valueOf(modified))
                        .bizNo(UUID.randomUUID().toString())
                        .build());

                log.info("{}扣减成功: 变更前金额:{}, 变更金额{}", userId, before.getAmount(), modified);
            } catch (Exception e) {
                log.error("余额扣减失败，userId:{}, 失败原因：{}", userId, e.getMessage());
            }
        }
    }
}
