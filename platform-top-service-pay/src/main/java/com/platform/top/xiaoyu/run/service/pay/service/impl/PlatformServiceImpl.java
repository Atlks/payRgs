package com.platform.top.xiaoyu.run.service.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.pay.vo.PayPlatformVO;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.payplatformrelease.PayPlatformPushReq;
import com.platform.top.xiaoyu.run.service.pay.entity.PayPlatform;
import com.platform.top.xiaoyu.run.service.pay.entity.PayPlatformRelease;
import com.platform.top.xiaoyu.run.service.pay.mapper.PayPlatformMapper;
import com.platform.top.xiaoyu.run.service.pay.service.IPayPlatformReleaseService;
import com.platform.top.xiaoyu.run.service.pay.service.IPayPlatformService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 第三方支付平台 服务实现类
 *
 * @author coffey
 */
@Service
public class PlatformServiceImpl extends AbstractMybatisPlusService<PayPlatformMapper, PayPlatform, Long> implements IPayPlatformService {

	@Autowired
	private PayPlatformMapper payPlatformMapper;
	@Autowired
	private IPayPlatformReleaseService releaseService;

	@Override
	public Page<PayPlatformVO> findPage(Page<PayPlatformVO> page, PayPlatformVO vo) {

		return payPlatformMapper.findPage(page, vo);
	}

	@Override
	public PayPlatformVO findDetail(Long id) {
		return payPlatformMapper.findDetail(PayPlatformVO.builder().id(id).build());
	}

	@Override
	public PayPlatformVO findDetail(PayPlatformVO vo) {
		return payPlatformMapper.findDetail(vo);
	}

	@Override
	public List<PayPlatformVO> findListAll(PayPlatformVO vo) {
		return payPlatformMapper.findListAll(vo);
	}

	@Override
	public boolean insert(PayPlatformVO vo) {
		return this.save(BeanCopyUtils.copyBean(vo, PayPlatform.class));
	}

	@Override
	public boolean update(PayPlatformVO vo) {
		return this.updateById(BeanCopyUtils.copyBean(vo, PayPlatform.class));
	}

	@Override
	public boolean delBatch(List<Long> ids) {
		return this.removeByIds(ids);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean push(PayPlatformPushReq req) {

		//查询支付平台数据
		QueryWrapper<PayPlatform> queryWrapper = new QueryWrapper<PayPlatform>();
		queryWrapper.eq(PayPlatform.PK_ID, req.getId());
		PayPlatform platform = this.getOne(queryWrapper);

		if(null == platform) {
			return false;
		}

		for (Long platformId : req.getPlatformList()) {
			//发布表中 支付平台ID 相关的数据
			QueryWrapper<PayPlatformRelease> queryWrapperGroup = new QueryWrapper<>();
			queryWrapperGroup.eq(PayPlatformRelease.P_PAY_PLATFORM_ID, req.getId())
					.eq(PayPlatformRelease.P_PLATFORM_ID, platformId);

			List<PayPlatformRelease> retList = releaseService.list(queryWrapperGroup);

			//true 当前数据需要添加
			boolean flagAdd = true;

			//迭代支付平台数据， 判断当前发布表中已经存在当前支付平台的数据
			for (PayPlatformRelease release : retList ) {
				//支付平台表主键ID  ==  支付发布平台的 支付平台主键ID true ： 数据已存在不执行新增
				if(release.getPayPlatformId().longValue() == platform.getId().longValue()) {
					flagAdd = false;
					continue;
				}
			}
			if(flagAdd) {
				//赋值 系统平台ID， 并把原来有主键ID赋NULL
				PayPlatformRelease release = BeanCopyUtils.copyBean(platform, PayPlatformRelease.class);
				release.setPayPlatformId(platform.getId());
				release.setId(null);
				release.setPlatformId(platformId);

				//新增
				releaseService.save(release);
			}
		}

		PayPlatform update_entity = new PayPlatform();
		update_entity.setId(req.getId());
		update_entity.setPushPlatformIds(req.getPlatformList().toString());

		return this.updateById(update_entity);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean cancelPush(PayPlatformPushReq req) {
		if (!CollectionUtils.isEmpty(req.getPlatformList())) {
			for (Long platformId : req.getPlatformList()) {

				//删除发布表中所有的 支付平台ID 相关的数据
				QueryWrapper<PayPlatformRelease> queryWrapper = new QueryWrapper<>();
				queryWrapper.eq(PayPlatformRelease.P_PAY_PLATFORM_ID, req.getId()).eq(PayPlatformRelease.P_PLATFORM_ID, platformId);

				releaseService.remove(queryWrapper);

			}

			PayPlatform update_entity = new PayPlatform();
			update_entity.setId(req.getId());
			update_entity.setPushPlatformIds(req.getPlatformList().toString());

			return this.updateById(update_entity);

		}
		return false;
	}

}
