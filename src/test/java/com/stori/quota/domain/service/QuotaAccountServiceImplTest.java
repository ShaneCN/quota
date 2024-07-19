package com.stori.quota.domain.service;

import com.stori.quota.domain.quota.model.QuotaAccount;
import com.stori.quota.domain.quota.model.QuotaAccountOptContext;
import com.stori.quota.domain.quota.repo.QuotaAccountRepo;
import com.stori.quota.domain.quota.service.QuotaAccountServiceImpl;
import com.stori.quota.infras.dao.mapper.UserAccountDOMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class QuotaAccountServiceImplTest {

    /** 额度账户仓储层 */
    @Mock
    private QuotaAccountRepo quotaAccountRepo;

    /** 事务模板 */
    @Mock
    private TransactionTemplate transactionTemplate;

    /** 用户账户DAO */
    @Mock
    private UserAccountDOMapper userAccountDOMapper;

    @InjectMocks
    private QuotaAccountServiceImpl quotaAccountService;

    private static final String mockUserId = "1001";

    private static final String mockAmount = "100.01";

    private static final BigDecimal mockDecimal = new BigDecimal(mockAmount);

    @Test
    public void testQuery() throws Exception {
        // given
        Mockito.when(quotaAccountRepo.queryByUserId(any())).thenReturn(QuotaAccount.init(mockUserId, mockDecimal));

        // when
        QuotaAccount query = quotaAccountService.query(QuotaAccountOptContext.builder().userId(mockUserId).build());

        // then
        Mockito.verify(quotaAccountRepo, Mockito.times(1)).queryByUserId(any());
        Assert.assertNotNull(query);
        Assert.assertEquals(query.getUserId(), mockUserId);
        Assert.assertEquals(query.getAmount(), mockDecimal);
    }

    @Test
    public void testInit() throws Exception {
        // given
        Mockito.when(quotaAccountRepo.queryByUserId(any())).thenReturn(null);
        Mockito.when(transactionTemplate.execute(any())).thenReturn(null);

        // when
        quotaAccountService.init(QuotaAccountOptContext.builder().userId(mockUserId).amount(mockDecimal).bizNo("bizNo").build());

        // then
        Mockito.verify(quotaAccountRepo, Mockito.times(1)).queryByUserId(any());

    }

    @Test
    public void testIncrease() throws Exception {
        // given
        Mockito.when(quotaAccountRepo.queryByUserId(any())).thenReturn(QuotaAccount.init(mockUserId, mockDecimal));
        Mockito.when(transactionTemplate.execute(any())).thenReturn(null);

        // when
        quotaAccountService.increse(QuotaAccountOptContext.builder().userId(mockUserId).amount(mockDecimal).bizNo("bizNo").build());

        // then
        Mockito.verify(quotaAccountRepo, Mockito.times(1)).queryByUserId(any());

    }

    @Test
    public void testDecrease() throws Exception {
        // given
        Mockito.when(quotaAccountRepo.queryByUserId(any())).thenReturn(QuotaAccount.init(mockUserId, mockDecimal));
        Mockito.when(transactionTemplate.execute(any())).thenReturn(null);

        // when
        quotaAccountService.decrease(QuotaAccountOptContext.builder().userId(mockUserId).amount(mockDecimal).bizNo("bizNo").build());

        // then
        Mockito.verify(quotaAccountRepo, Mockito.times(1)).queryByUserId(any());

    }

}
