package com.stori.quota.biz;

import com.stori.quota.domain.quota.model.QuotaAccount;
import com.stori.quota.domain.quota.service.QuotaAccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserQuotaServiceTest {

    @Mock
    private QuotaAccountService quotaAccountService;

    @InjectMocks
    private UserQuotaService userQuotaService;

    private static final String mockUserId = "1001";

    private static final String amount = "100.01";

    @Before
    public void init() throws Exception {
        Mockito.when(quotaAccountService.query(any())).thenReturn(QuotaAccount.init(mockUserId, new BigDecimal(amount)));

    }

    /**
     * 查询
     */
    @Test
    public void query() throws Exception {
        // given

        // when
        QuotaAccount query = userQuotaService.query(mockUserId);

        // then
        Mockito.verify(quotaAccountService, Mockito.times(1)).query(any());
        Assertions.assertEquals(mockUserId, query.getUserId());
        Assertions.assertEquals(amount, query.getAmount().toString());
    }

    @Test(expected = Exception.class)
    public void queryNoAccount() throws Exception {
        // given
        Mockito.doThrow(new Exception()).when(quotaAccountService).query(any());

        // when
        QuotaAccount query = userQuotaService.query(mockUserId);

        // then
        Mockito.verify(quotaAccountService, Mockito.times(1)).query(any());
    }


    @Test
    public void openAccount() throws Exception {
        // given
        Mockito.doNothing().when(quotaAccountService).init(any());

        // when
        QuotaAccount query = userQuotaService.openAccount(mockUserId, amount);

        // then
        Mockito.verify(quotaAccountService, Mockito.times(1)).query(any());
        Mockito.verify(quotaAccountService, Mockito.times(1)).init(any());

        Assertions.assertEquals(mockUserId, query.getUserId());
        Assertions.assertEquals(amount, query.getAmount().toString());

    }

    @Test
    public void flowIn() throws Exception {
        // given
        Mockito.doNothing().when(quotaAccountService).increse(any());

        // when
        QuotaAccount quotaAccount = userQuotaService.flowIn(mockUserId, amount);

        // then
        Assertions.assertEquals(mockUserId, quotaAccount.getUserId());
        Assertions.assertEquals(amount, quotaAccount.getAmount().toString());

        Mockito.verify(quotaAccountService, Mockito.times(1)).query(any());
        Mockito.verify(quotaAccountService, Mockito.times(1)).increse(any());
    }

    @Test
    public void flowOut() throws Exception {
        // given
        Mockito.doNothing().when(quotaAccountService).decrease(any());

        // when
        QuotaAccount quotaAccount = userQuotaService.flowOut(mockUserId, amount);

        // then
        Assertions.assertEquals(mockUserId, quotaAccount.getUserId());
        Assertions.assertEquals(amount, quotaAccount.getAmount().toString());

        Mockito.verify(quotaAccountService, Mockito.times(1)).query(any());
        Mockito.verify(quotaAccountService, Mockito.times(1)).decrease(any());

    }
}