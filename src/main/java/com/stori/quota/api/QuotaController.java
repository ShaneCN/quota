package com.stori.quota.api;

import com.stori.quota.biz.UserQuotaService;
import com.stori.quota.domain.quota.model.QuotaAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Random;

/**
 * 额度管理controller
 */
@Controller
public class QuotaController {

    /** 用户额度服务 */
    @Resource
    private UserQuotaService userQuotaService;

    /**
     * 开户，额度账户初始化
     *
     * @param userId 用户id
     * @param amount 金额
     * @return 开户结果
     */
    @RequestMapping(value = "/open/user/{userId}/amount/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result<QuotaAccount> open(@PathVariable("userId") String userId, @PathVariable("amount") String amount) {
        Result<QuotaAccount> result = new Result<>();
        try {
            result.setT(userQuotaService.openAccount(userId, amount));
            result.success("开户成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.fail(e.getMessage());
        }
        return result;
    }

    /**
     * 额度增加
     *
     * @param userId 用户id
     * @param amount 额度
     * @return 结果
     */
    @RequestMapping(value = "/flowin/user/{userId}/amount/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result<QuotaAccount> flowIn(@PathVariable("userId") String userId, @PathVariable("amount") String amount) {
        Result<QuotaAccount> result = new Result<>();
        try {
            result.setT(userQuotaService.flowIn(userId, amount));
            result.success("额度增加成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.fail(e.getMessage());
        }
        return result;
    }

    /**
     * 额度增加
     *
     * @param userId 用户id
     * @param amount 额度
     * @return 结果
     */
    @RequestMapping(value = "/flowout/user/{userId}/amount/{amount}", method = RequestMethod.GET)
    @ResponseBody
    public Result<QuotaAccount> flowOut(@PathVariable("userId") String userId, @PathVariable("amount") String amount) {
        Result<QuotaAccount> result = new Result<>();
        try {
            result.setT(userQuotaService.flowOut(userId, amount));
            result.success("额度扣减成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.fail(e.getMessage());
        }
        return result;
    }

    /**
     * 额度增加
     *
     * @param userId 用户id
     * @return 结果
     */
    @RequestMapping(value = "/query/user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public Result<QuotaAccount> query(@PathVariable("userId") String userId) {
        Result<QuotaAccount> result = new Result<>();
        try {
            result.setT(userQuotaService.query(userId));
            result.success("账户查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.fail(e.getMessage());
        }
        return result;
    }



    /**
     * 批量初始化账户
     * 1000000开始，初始化20个，随机金额
     *
     * @return 开户结果
     */
    @RequestMapping(value = "/openbatch", method = RequestMethod.GET)
    @ResponseBody
    public Result<QuotaAccount> openBatch() {
        Result<QuotaAccount> result = new Result<>();
        try {
            int startId = 1000000;
            Random random = new Random();

            for (int i=0; i<20; i++) {
                String userId = String.valueOf(startId+i);
                String amount = String.valueOf(random.nextInt(1000));

                userQuotaService.openAccount(userId, amount);
            }

            result.success("开户成功");
        } catch (Exception e) {
            e.printStackTrace();
            result.fail(e.getMessage());
        }
        return result;
    }
}
