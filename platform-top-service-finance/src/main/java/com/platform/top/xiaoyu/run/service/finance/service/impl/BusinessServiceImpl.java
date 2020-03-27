package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.enums.*;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BusinessVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountToolsVO;
import com.platform.top.xiaoyu.run.service.finance.entity.Business;
import com.platform.top.xiaoyu.run.service.finance.mapper.BusinessMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IBusinessService;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountToolsService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 交易记录 服务实现类
 *
 * @author coffey
 */
@Service
public class BusinessServiceImpl extends AbstractMybatisPlusService<BusinessMapper, Business, Long> implements IBusinessService {

	@Autowired
	private BusinessMapper mapper;
	@Autowired
	private IReceiptAccountToolsService toolsService;

	@Override
	public Page<BusinessVO> findPage(Page<BusinessVO> page, BusinessVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findPage(page, vo);
	}

	@Override
	public BusinessVO findDetail(BusinessVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findDetail(vo);
	}

	@Override
	public BusinessVO findDetailId(Long id, Long platformId) {
		BusinessVO vo = new BusinessVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return findDetail(vo);
	}

	@Override
	public List<BusinessVO> findListAll(BusinessVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return mapper.findListAll(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(BusinessVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return this.save(BeanCopyUtils.copyBean(setBusVO(vo), Business.class));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean update(BusinessVO vo) {
		if( null != vo && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		//解析枚举字段
		vo = this.setBusVO(vo);
		UpdateWrapper<Business> updateWrapper = new UpdateWrapper<Business>();
		updateWrapper.eq(Business.PK_ID, vo.getId()).eq(Business.P_PLATFORM_ID, vo.getPlatformId());
		return this.update(BeanCopyUtils.copyBean(vo, Business.class), updateWrapper);
	}

	/**
	 * 设置所有枚举字段显示在前端的str
	 * @param businessVO
	 * @return
	 */
	private BusinessVO setBusVO(BusinessVO businessVO) {
		if( null != businessVO) {
			if( null != businessVO.getStatuss() ) {
				businessVO.setStatussStr(BusStatusEnums.getType(businessVO.getStatuss()).getName());
			}
			if( null != businessVO.getType() ) {
				businessVO.setTypeStr(BusTypeEnums.getType(businessVO.getType()).getName());
			}
			if( null != businessVO.getTypeAll() ) {
				businessVO.setTypeAllStr(BusTypeAllEnums.getType(businessVO.getTypeAll()).getName());
			}
			if( null != businessVO.getPayStatus() ) {
				businessVO.setPayStatusStr(FsPayStatusEnums.getType(businessVO.getPayStatus()).getName());
			}

			BusTradingMannerEnums trmangEnums = BusTradingMannerEnums.getType(businessVO.getTradingManner());
			businessVO.setTradingMannerStr(trmangEnums != null ? trmangEnums.getName() : null);
			ReceiptAccountToolsVO toolsVO = toolsService.findDetailType(businessVO.getTradingManner(), businessVO.getPlatformId());

			if( null != toolsVO) {
				businessVO.setTradingMannerStr(toolsVO.getName());
				businessVO.setTradingMannerOffStr(toolsVO.getOfferStr());
			}
		}
		return businessVO;
	}
}
