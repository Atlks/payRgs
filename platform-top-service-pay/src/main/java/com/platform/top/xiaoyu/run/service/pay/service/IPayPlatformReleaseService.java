package com.platform.top.xiaoyu.run.service.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayPlatformReleaseVO;
import com.platform.top.xiaoyu.run.service.pay.entity.PayPlatformRelease;

import java.util.List;

/**
 * 第三方支付发布平台
 *
 * @author coffey
 */
public interface IPayPlatformReleaseService extends IService<PayPlatformRelease> {

    public Page<PayPlatformReleaseVO> findPage(Page<PayPlatformReleaseVO> page, PayPlatformReleaseVO vo);

    public PayPlatformReleaseVO findDetail(Long id, Long platformId);

    public PayPlatformReleaseVO findDetail(PayPlatformReleaseVO vo);

    public List<PayPlatformReleaseVO> findListAll(PayPlatformReleaseVO vo);

    public boolean insert(PayPlatformReleaseVO vo);

    public boolean update(PayPlatformReleaseVO vo);

    public boolean delBatch(List<Long> ids, Long platformId);

}
