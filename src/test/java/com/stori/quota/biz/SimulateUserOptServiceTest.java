package com.stori.quota.biz;


import com.stori.quota.domain.quota.service.QuotaAccountService;
import com.stori.quota.infras.dao.entity.UserAccountDO;
import com.stori.quota.infras.dao.mapper.UserAccountDOMapper;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

/**
 * 定时操作测试用例
 * 由于是模拟随机操作，无法编写测试用例
 */

@RunWith(MockitoJUnitRunner.class)
public class SimulateUserOptServiceTest {

    @Mock
    private QuotaAccountService quotaAccountService;

    @Mock
    private UserAccountDOMapper mapper;

    @InjectMocks
    private SimulateUserOptService simulateUserOptService;

    @Before
    public void init() {
        Mockito.when(mapper.selectAllUsers()).thenReturn(buildAccountList());
    }


    @Test
    public void testRandomOpt_no_user() throws Exception {
        // given
        Mockito.when(mapper.selectAllUsers()).thenReturn(new ArrayList<>());

        // when
        simulateUserOptService.randomOpt();

        // then
        Mockito.verify(quotaAccountService, Mockito.times(0)).query(any());

    }

    private List<UserAccountDO> buildAccountList() {
        return Lists.newArrayList(UserAccountDO.builder().userId("1001").build(), UserAccountDO.builder().userId("1002").build());
    }
}
