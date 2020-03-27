package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.enums.ToolsActTypeEnums;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountToolsVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountVO;
import com.platform.top.xiaoyu.run.service.finance.entity.ReceiptAccount;
import com.platform.top.xiaoyu.run.service.finance.mapper.ReceiptAccountMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountService;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountToolsService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 收款账号 服务实现类
 *
 * @author coffey
 */
@Service
public class ReceiptAccountServiceImpl extends AbstractMybatisPlusService<ReceiptAccountMapper, ReceiptAccount, Long> implements IReceiptAccountService {

	@Autowired
	private ReceiptAccountMapper receiptAccountMapper;
	@Autowired
	private IReceiptAccountToolsService toolsService;

	@Override
	public Page<ReceiptAccountVO> findPage(Page<ReceiptAccountVO> page, ReceiptAccountVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return receiptAccountMapper.findPage(page, vo);
	}

	@Override
	public List<ReceiptAccountVO> findListALL(ReceiptAccountVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return receiptAccountMapper.findListAll(vo);
	}

	@Override
	public ReceiptAccountVO findDetail(ReceiptAccountVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return receiptAccountMapper.findDetail(vo);
	}

	@Override
	public ReceiptAccountVO findDetail(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return receiptAccountMapper.findDetail(ReceiptAccountVO.builder().id(id).build());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(ReceiptAccountVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		//查询配置ID 数据存在
		ReceiptAccountToolsVO toolsVO = toolsService.findDetail(vo.getToolsId(), vo.getPlatformId());
		if(null == toolsVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_ACT_TOOLS_NULL);
		}
		//设置 账号类型 str
		if(null != vo.getAccountType() && vo.getAccountType() > 0 ) {
			vo.setAccountTypeStr(ToolsActTypeEnums.getType(vo.getAccountType()).getName());
		}
		return this.save(BeanCopyUtils.copyBean(vo, ReceiptAccount.class));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateIsDefault(Long id, Long userId, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		ReceiptAccountVO find_default = new ReceiptAccountVO();

		//查询默认绑卡记录
		find_default.setId(userId);
		find_default.setPlatformId(platformId);
		find_default.setStatuss(CommonStatus.ENABLE.getValue());
		find_default = this.findDetail(find_default);

		ReceiptAccount update_notdefault = new ReceiptAccount();
		update_notdefault.setStatuss(CommonStatus.DISABLE.getValue());
		update_notdefault.setId(find_default.getId());

		UpdateWrapper<ReceiptAccount> updateWrapper = new UpdateWrapper<ReceiptAccount>();
		updateWrapper.eq(ReceiptAccount.PK_ID, find_default.getId()).eq(ReceiptAccount.P_PLATFORM_ID, platformId);

		this.update(update_notdefault, updateWrapper);

		//设置默认绑卡记录
		ReceiptAccount update_default = new ReceiptAccount();
		update_default.setId(id);
		update_default.setUpdateTimestamp(LocalDateTime.now());
		update_default.setStatuss(CommonStatus.ENABLE.getValue());

		UpdateWrapper<ReceiptAccount> updateWrapper_default = new UpdateWrapper<ReceiptAccount>();
		updateWrapper_default.eq(ReceiptAccount.PK_ID, find_default.getId()).eq(ReceiptAccount.P_PLATFORM_ID, platformId);

		return this.update(update_default, updateWrapper_default);

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean del(List<Long> ids, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		QueryWrapper<ReceiptAccount> queryWrapper = new QueryWrapper<ReceiptAccount>();
		queryWrapper.in(ReceiptAccount.PK_ID, ids).eq(ReceiptAccount.P_PLATFORM_ID, platformId);
		return this.remove(queryWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateStatus(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		ReceiptAccount entity = new ReceiptAccount();
		entity.setIsDeleted(CommonStatus.ENABLE.getValue());
		entity.setUpdateTimestamp(LocalDateTime.now());
		entity.setId(id);
		//查询当前数据
		ReceiptAccountVO vo = this.findDetail(id, platformId);
		if(null == vo) {
			return false;
		}
		if(CommonStatus.getCommonStatus(vo.getIsDeleted()) == CommonStatus.ENABLE) {
			entity.setIsDeleted(CommonStatus.DISABLE.getValue());
		}
		//更新状态
		UpdateWrapper<ReceiptAccount> updateWrapper = new UpdateWrapper<ReceiptAccount>();
		updateWrapper.eq(ReceiptAccount.PK_ID, id).eq(ReceiptAccount.P_PLATFORM_ID, platformId);

		return this.update(entity, updateWrapper);
	}


}
