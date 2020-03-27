package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountToolsVO;
import com.platform.top.xiaoyu.run.service.api.finance.vo.ReceiptAccountValueVO;
import com.platform.top.xiaoyu.run.service.finance.entity.ReceiptAccountValue;
import com.platform.top.xiaoyu.run.service.finance.mapper.ReceiptAccountValueMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountToolsService;
import com.platform.top.xiaoyu.run.service.finance.service.IReceiptAccountValueService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 收款账号 服务实现类
 *
 * @author coffey
 */
@Service
public class ReceiptAccountValueServiceImpl extends AbstractMybatisPlusService<ReceiptAccountValueMapper, ReceiptAccountValue, Long> implements IReceiptAccountValueService {

	@Autowired
	private ReceiptAccountValueMapper receiptAccountValueMapper;

	@Autowired
	private IReceiptAccountToolsService toolsService;

	@Override
	public Page<ReceiptAccountValueVO> findPage(Page<ReceiptAccountValueVO> page, ReceiptAccountValueVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return receiptAccountValueMapper.findPage(page, vo);
	}

	@Override
	public List<ReceiptAccountValueVO> findListALL(ReceiptAccountValueVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return receiptAccountValueMapper.findListAll(vo);
	}

	@Override
	public ReceiptAccountValueVO findDetail(ReceiptAccountValueVO vo) {
		if( null != vo && null != vo.getPlatformId() && vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return receiptAccountValueMapper.findDetail(vo);
	}

	@Override
	public ReceiptAccountValueVO findDetail(Long id, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		ReceiptAccountValueVO vo = new ReceiptAccountValueVO();
		vo.setId(id);
		vo.setPlatformId(platformId);
		return findDetail(vo);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insertBatch(Long toolsId, Long platformId, List<Integer> list) {
		//查询配置ID 数据存在
		ReceiptAccountToolsVO toolsVO = toolsService.findDetail(toolsId, platformId);
		if(null == toolsVO) {
			throw new BizBusinessException(BaseExceptionType.DATA_ACT_TOOLS_NULL);
		}

		//删除当前配置下所有记录
		QueryWrapper<ReceiptAccountValue> queryWrapper = new QueryWrapper<ReceiptAccountValue>();
		queryWrapper.eq(ReceiptAccountValue.P_TOOLS_ID, toolsId).eq(ReceiptAccountValue.P_PLATFORM_ID, platformId);
		this.remove(queryWrapper);

		//批量新增
		if(!CollectionUtils.isEmpty(list)) {
			List<ReceiptAccountValue> addList = new ArrayList<>();
			for (Integer integer : list ) {
				ReceiptAccountValue entity = new ReceiptAccountValue();
				entity.setToolsId(toolsId);
				entity.setValue(integer);
				entity.setPlatformId(platformId);
				entity.setCreateTimestamp(LocalDateTime.now());
				entity.setUpdateTimestamp(LocalDateTime.now());
				addList.add(entity);
			}
			return this.saveBatch(addList);
		}
		return false;
	}

	@Override
	public boolean del(List<Long> ids, Long platformId) {
		if( null != platformId && platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		QueryWrapper<ReceiptAccountValue> queryWrapper = new QueryWrapper<ReceiptAccountValue>();
		queryWrapper.in(ReceiptAccountValue.PK_ID, ids).eq(ReceiptAccountValue.P_PLATFORM_ID, platformId);
		return this.remove(queryWrapper);
	}

}
