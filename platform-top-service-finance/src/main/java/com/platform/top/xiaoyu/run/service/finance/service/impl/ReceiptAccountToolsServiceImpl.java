package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountToolsVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountValueVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.resp.recharge.RechargeTypeResp;
import com.platform.top.xiaoyu.run.service.finance.entity.ReceiptAccountTools;
import com.platform.top.xiaoyu.run.service.finance.mapper.ReceiptAccountToolsMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountService;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountToolsService;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountValueService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 收款账号 服务实现类
 *
 * @author coffey
 */
@Service
public class ReceiptAccountToolsServiceImpl extends AbstractMybatisPlusService<ReceiptAccountToolsMapper, ReceiptAccountTools, Long> implements IReceiptAccountToolsService {

	@Autowired
	private ReceiptAccountToolsMapper mapper;
	@Autowired
	private IReceiptAccountValueService valueService;
	@Autowired
	private IReceiptAccountService accountService;


	@Override
	public Page<ReceiptAccountToolsVO> findPage(Page<ReceiptAccountToolsVO> page, ReceiptAccountToolsVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findPage(page, vo);
	}

	@Override
	public List<ReceiptAccountToolsVO> findListALL(ReceiptAccountToolsVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findListAll(vo);
	}

	@Override
	public ReceiptAccountToolsVO findDetail(ReceiptAccountToolsVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findDetail(vo);
	}

	@Override
	public ReceiptAccountToolsVO findDetailType(Integer type, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return  mapper.findDetail(ReceiptAccountToolsVO.builder().type(type).platformId(platformId).build());
	}

	@Override
	public ReceiptAccountToolsVO findDetail(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findDetail(ReceiptAccountToolsVO.builder().id(id).platformId(platformId).build());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(ReceiptAccountToolsVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return this.save(BeanCopyUtils.copyBean(vo, ReceiptAccountTools.class));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean del(List<Long> ids, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		QueryWrapper<ReceiptAccountTools> queryWrapper = new QueryWrapper<ReceiptAccountTools>();
		queryWrapper.in(ReceiptAccountTools.PK_ID, ids).eq(ReceiptAccountTools.P_PLATFORM_ID, platformId);
		return this.remove(queryWrapper);
	}

	@Override
	public List<RechargeTypeResp> findCombox(List<ReceiptAccountToolsVO> list, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}

		//返回所有配置下Value值、 账号
		List<RechargeTypeResp> retList = new ArrayList<RechargeTypeResp>();
		for (ReceiptAccountToolsVO vo : list) {

			RechargeTypeResp resp = BeanCopyUtils.copyBean(vo, RechargeTypeResp.class);

			//查询当前设置下的所有value
			List<Integer> valueList = this.getValue(vo.getId(), platformId);
			resp.setListValue(valueList);

			ReceiptAccountVO accountVO = new ReceiptAccountVO();
			accountVO.setIsDeleted(CommonStatus.ENABLE.getValue());
			accountVO.setPlatformId(platformId);
			accountVO.setToolsId(vo.getId());
			//查询当前设置下所有账号
			resp.setAccountList(accountService.findListALL(accountVO));

			retList.add(resp);
		}
		return retList;
	}

	private List<Integer> getValue(Long toolsId, Long platformId) {
		ReceiptAccountValueVO findValueVO = new ReceiptAccountValueVO();
		findValueVO.setToolsId(toolsId);
		findValueVO.setPlatformId(platformId);
		List<ReceiptAccountValueVO> valueVOS = valueService.findListALL(findValueVO);
		List<Integer> valueList = new ArrayList<Integer>();
		if (!CollectionUtils.isEmpty(valueVOS)) {
			for (ReceiptAccountValueVO valueVO : valueVOS) {
				valueList.add(valueVO.getValue());
			}
		}
		return valueList;
	}

}
