package com.stori.quota.domain.quota.repo;

import com.stori.quota.domain.quota.model.QuotaAccount;
import com.stori.quota.domain.quota.model.QuotaAccountOptContext;
import com.stori.quota.infras.dao.entity.AccountOptFlowDO;
import com.stori.quota.infras.dao.entity.UserAccountDO;
import com.stori.quota.infras.dao.mapper.AccountOptFlowDOMapper;
import com.stori.quota.infras.dao.mapper.UserAccountDOMapper;
import com.stori.quota.infras.enums.OptTypeEnum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Objects;

@Component
public class QuotaAccountRepoImpl implements QuotaAccountRepo {

    @Resource
    private UserAccountDOMapper userAccountDOMapper;

    @Resource
    private AccountOptFlowDOMapper accountOptFlowDOMapper;

    @Override
    public QuotaAccount queryByUserId(String userId) {
        return QuotaAccount.fromDO(userAccountDOMapper.selectByUserId(userId));
    }

    @Override
    public QuotaAccount lockByUserId(String userId) {
        return null;
    }

    @Override
    public void store(QuotaAccount account, String bizNo) throws Exception {
        UserAccountDO insertDO = QuotaAccount.toDO(account);
        if (Objects.isNull(insertDO)) {
            throw new Exception("尝试插入空记录");
        }
        userAccountDOMapper.insert(insertDO);
        UserAccountDO queryDO = userAccountDOMapper.selectByUserId(account.getUserId());

        AccountOptFlowDO accountOptFlowDO = new AccountOptFlowDO();
        accountOptFlowDO.setBizNo(bizNo);
        accountOptFlowDO.setAccountId(queryDO.getId().toString());
        accountOptFlowDO.setType(OptTypeEnum.INIT.getCode());
        accountOptFlowDO.setUserId(insertDO.getUserId());
        accountOptFlowDO.setBeforeAmount(BigDecimal.ZERO);
        accountOptFlowDO.setModifiedAmount(insertDO.getAmount());
        accountOptFlowDO.setAfterAmount(insertDO.getAmount());
        accountOptFlowDO.setGmtCreate(insertDO.getGmtCreate());
        accountOptFlowDO.setGmtModified(insertDO.getGmtModified());

        accountOptFlowDOMapper.insert(accountOptFlowDO);
    }

    @Override
    public void update(QuotaAccount account, QuotaAccountOptContext context, OptTypeEnum optType) throws Exception {
        UserAccountDO updateAccountDO = QuotaAccount.toDO(account);
        if (Objects.isNull(updateAccountDO)) {
            throw new Exception("更新记录不能为空");
        }
        AccountOptFlowDO accountOptFlowDO = new AccountOptFlowDO();
        accountOptFlowDO.setBizNo(context.getBizNo());
        accountOptFlowDO.setAccountId(updateAccountDO.getId().toString());
        accountOptFlowDO.setUserId(updateAccountDO.getUserId());
        accountOptFlowDO.setType(optType.getCode());
        accountOptFlowDO.setAfterAmount(updateAccountDO.getAmount());
        accountOptFlowDO.setModifiedAmount(context.getAmount());
        accountOptFlowDO.setGmtCreate(updateAccountDO.getGmtModified());
        accountOptFlowDO.setGmtModified(updateAccountDO.getGmtModified());

        userAccountDOMapper.updateByPrimaryKey(updateAccountDO);
        accountOptFlowDOMapper.insert(accountOptFlowDO);

    }
}
