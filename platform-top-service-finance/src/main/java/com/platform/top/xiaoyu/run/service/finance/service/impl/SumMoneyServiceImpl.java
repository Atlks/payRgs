package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.platform.top.xiaoyu.run.service.api.finance.enums.SumMoneyTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.finance.entity.SumMoney;
import com.platform.top.xiaoyu.run.service.finance.mapper.SumMoneyMapper;
import com.platform.top.xiaoyu.run.service.finance.service.ISumMoneyService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 金额统计 服务实现类
 *
 * @author coffey
 */
@Service
public class SumMoneyServiceImpl extends AbstractMybatisPlusService<SumMoneyMapper, SumMoney, Long> implements ISumMoneyService {

    @Autowired
    private SumMoneyMapper sumMoneyMapper;

    @Override
    public SumMoney findDetailUserId(Long userId, Long platformId) {
        SumMoney vo = new SumMoney();
        vo.setPlatformId(platformId);
        vo.setUserId(userId);
        return this.findDetail(vo);
    }

    @Override
    public SumMoney findDetailUserIdType(Long userId, Long platformId, SumMoneyTypeEnums type) {
        SumMoney vo = new SumMoney();
        vo.setPlatformId(platformId);
        vo.setUserId(userId);
        vo.setType(type.getVal());
        return this.findDetail(vo);
    }

    @Override
    public SumMoney findDetail(SumMoney vo) {
        if(null == vo || null == vo.getPlatformId() || vo.getPlatformId().longValue() <= 0 ) {
            throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
        }
        return sumMoneyMapper.findDetail(vo);
    }
}
