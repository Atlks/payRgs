package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTradingMannerEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeAllEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.BusTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.enums.SafeFlowEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.exception.SafeExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BookVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.FlowVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.SafeFlowVO;
import com.platform.top.xiaoyu.run.service.finance.constant.BusConstant;
import com.platform.top.xiaoyu.run.service.finance.entity.SafeFlow;
import com.platform.top.xiaoyu.run.service.finance.mapper.SafeFlowMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IBookService;
import com.platform.top.xiaoyu.run.service.finance.service.IFlowService;
import com.platform.top.xiaoyu.run.service.finance.service.ISafeFlowService;
import com.top.xiaoyu.rearend.component.lock.api.annotation.Lock;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 保险箱流水 服务实现类
 *
 * @author coffey
 */
@Service
public class SafeFlowServiceImpl extends AbstractMybatisPlusService<SafeFlowMapper, SafeFlow, Long> implements ISafeFlowService {

	@Autowired
	private SafeFlowMapper safeFlowMapper;
	@Autowired
	private IBookService iBookService;
	@Autowired
	private IFlowService flowService;

	@Override
	public Page<SafeFlowVO> findPage(Page<SafeFlowVO> page, SafeFlowVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return safeFlowMapper.findPage(page, BeanCopyUtils.copyBean(vo, SafeFlowVO.class));
	}

	@Override
	public List<SafeFlowVO> findListAll(SafeFlowVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return safeFlowMapper.findListAll(vo);
	}

	@Override
	public SafeFlowVO findDetail(SafeFlowVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return safeFlowMapper.findDetail(vo);
	}

	@Override
	public SafeFlowVO findDetailId(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		SafeFlowVO vo = new SafeFlowVO();
		vo.setId(id);
		return safeFlowMapper.findDetail(vo);
	}


	@Override
	@Lock("coffey-finance-userId-#{#safeFlowVO.userId}")
	@Transactional(rollbackFor = Exception.class)
	public boolean insertFlowIn(SafeFlowVO safeFlowVO) {
		if( null != safeFlowVO && null != safeFlowVO.getPlatformId() && safeFlowVO.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		Long idcode = idService.getNextId();

		/** 查询 我的账本 */
		BookVO bookVO = iBookService.findDetail(safeFlowVO.getUserId(), safeFlowVO.getPlatformId());

		if(null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}
		String amountStr = safeFlowVO.getAmount();

		/** 可用余额 < 转入金额 */
		if( new BigDecimal(bookVO.getBalance()).longValue() < new BigDecimal(amountStr).longValue()) {
			throw new BizBusinessException(SafeExceptionType.FLOW_BALANCE_IN);
		}
		/** 累减可用余额， 累加保险金额 */
		if( !iBookService.addBalanceSafeIn(bookVO.getId(),safeFlowVO.getPlatformId(), amountStr) ) {
			throw new BizBusinessException(SafeExceptionType.BOOK_BALANCE_FAIL);
		}
		BookVO bookVOBalance = iBookService.findDetail(safeFlowVO.getUserId(), safeFlowVO.getPlatformId());
		safeFlowVO.setBalanceSafe(bookVOBalance.getBalanceSafe());
		safeFlowVO.setBalance(bookVOBalance.getBalance());

		LocalDateTime date = LocalDateTime.now();
		safeFlowVO.setCreateTimestamp(date);
		safeFlowVO.setUpdateTimestamp(date);
		safeFlowVO.setType(SafeFlowEnums.TYPE_IN.getVal());
		safeFlowVO.setId(idcode);
		safeFlowVO.setPlatformId(safeFlowVO.getPlatformId());
		safeFlowVO.setRemarks(BusConstant.SAFE_IN);

		/** 插入转入流水记录 */
		int count = safeFlowMapper.insert(BeanCopyUtils.copyBean(safeFlowVO, SafeFlow.class));

		if(count <= 0) {
			throw new BizBusinessException(SafeExceptionType.FLOW_IN_FAIL);
		}

		/**  插入交易流水 */
		FlowVO flowVO = new FlowVO();
		flowVO.setAmount(amountStr);
		flowVO.setActualAmount(amountStr);
		flowVO.setUserId(safeFlowVO.getUserId());
		flowVO.setPlatformId(safeFlowVO.getPlatformId());
		flowVO.setBusTimestamp(LocalDateTime.now());
		flowVO.setTypeAll(BusTypeAllEnums.BALANCE_LESS.getVal());
		flowVO.setType(BusTypeEnums.SAFE_IN.getVal());
		flowVO.setCodeId(idcode);
		flowVO.setCode(String.valueOf(idcode));
		flowVO.setTradingManner(BusTradingMannerEnums.BUS_TRA_ADMIN_OUT.getVal());
		flowVO.setRemark(BusConstant.SAFE_IN);
		flowVO.setDescription(BusConstant.SAFE_IN);
		flowService.insertFlow(flowVO);

		return true;
	}

	@Override
	@Lock("coffey-finance-userId-#{#safeFlowVO.userId}")
	@Transactional(rollbackFor = Exception.class)
	public boolean insertFlowOut(SafeFlowVO safeFlowVO) {
		if( null != safeFlowVO && null != safeFlowVO.getPlatformId() && safeFlowVO.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		Long idcode = idService.getNextId();

		/** 查询 我的账本 */
		BookVO bookVO = iBookService.findDetail(safeFlowVO.getUserId(), safeFlowVO.getPlatformId());
		if(null == bookVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_NULL);
		}

		String amountStr = safeFlowVO.getAmount();

		/** 保险箱金额 < 转出金额 */
		if( new BigDecimal(bookVO.getBalanceSafe()).longValue() < new BigDecimal(amountStr).longValue() ) {
			throw new BizBusinessException(SafeExceptionType.FLOW_BALANCE_OUT);
		}
		/** 累加可用余额， 累减保险金额 */
		if( !iBookService.addBalanceSafeOut(bookVO.getId(), safeFlowVO.getPlatformId(), amountStr) ) {
			throw new BizBusinessException(SafeExceptionType.BOOK_BALANCE_FAIL);
		}

		BookVO bookVOBalance = iBookService.findDetail(safeFlowVO.getUserId(), safeFlowVO.getPlatformId());
		safeFlowVO.setBalanceSafe(bookVOBalance.getBalanceSafe());
		safeFlowVO.setBalance(bookVOBalance.getBalance());

		safeFlowVO.setId(idcode);
		safeFlowVO.setPlatformId(safeFlowVO.getPlatformId());

		LocalDateTime date = LocalDateTime.now();
		safeFlowVO.setCreateTimestamp(date);
		safeFlowVO.setUpdateTimestamp(date);
		safeFlowVO.setType(SafeFlowEnums.TYPE_OUT.getVal());
		safeFlowVO.setRemarks(BusConstant.SAFE_OUT);

		/** 插入转出流水 */
		int count = safeFlowMapper.insert(BeanCopyUtils.copyBean(safeFlowVO, SafeFlow.class));

		if(count <= 0) {
			throw new BizBusinessException(SafeExceptionType.FLOW_OUT_FAIL);
		}

		/**  插入交易流水 */
		FlowVO flowVO = new FlowVO();
		flowVO.setCreateTimestamp(date);
		flowVO.setAmount(amountStr);
		flowVO.setActualAmount(amountStr);
		flowVO.setUserId(safeFlowVO.getUserId());
		flowVO.setPlatformId(safeFlowVO.getPlatformId());
		flowVO.setBusTimestamp(date);
		flowVO.setTypeAll(BusTypeAllEnums.BALANCE_CALC.getVal());
		flowVO.setType(BusTypeEnums.SAFE_OUT.getVal());
		flowVO.setCodeId(idcode);
		flowVO.setCode(String.valueOf(idcode));
		flowVO.setTradingManner(BusTradingMannerEnums.BUS_TRA_ADMIN_IN.getVal());
		flowVO.setRemark(BusConstant.SAFE_OUT);
		flowVO.setDescription(BusConstant.SAFE_OUT);
		flowService.insertFlow(flowVO);

		return true;
	}



}
