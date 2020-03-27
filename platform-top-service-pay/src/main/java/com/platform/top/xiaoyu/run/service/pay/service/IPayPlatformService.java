package com.platform.top.xiaoyu.run.service.pay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayPlatformVO;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.payplatformrelease.PayPlatformPushReq;
import com.platform.top.xiaoyu.run.service.pay.entity.PayPlatform;

import java.util.List;

/**
 * 第三方支付平台
 *
 * @author coffey
 */
public interface IPayPlatformService extends IService<PayPlatform> {

    public Page<PayPlatformVO> findPage(Page<PayPlatformVO> page, PayPlatformVO vo);

    public PayPlatformVO findDetail(Long id);

    public PayPlatformVO findDetail(PayPlatformVO vo);

    public List<PayPlatformVO> findListAll(PayPlatformVO vo);

    public boolean insert(PayPlatformVO vo);

    public boolean update(PayPlatformVO vo);

    public boolean delBatch(List<Long> ids);

    /**
     * 发布到平台
     * @param req
     * @return
     */
    public boolean push(PayPlatformPushReq req);

    /**
     * 取消发布平台
     * @param req
     * @return
     */
    public boolean cancelPush(PayPlatformPushReq req);

}
