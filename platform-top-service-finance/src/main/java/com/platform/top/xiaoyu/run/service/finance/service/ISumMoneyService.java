package com.platform.top.xiaoyu.run.service.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.finance.enums.SumMoneyTypeEnums;
import com.platform.top.xiaoyu.run.service.finance.entity.SumMoney;

/**
 * 金额统计
 * @author coffey
 */
public interface ISumMoneyService extends IService<SumMoney> {

    public SumMoney findDetailUserId(Long userId, Long platformId);

    public SumMoney findDetailUserIdType(Long userId, Long platformId, SumMoneyTypeEnums type);

    public SumMoney findDetail(SumMoney vo);

}
