package com.platform.top.xiaoyu.run.service.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.platform.top.xiaoyu.run.service.api.common.enums.CommonStatus;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.finance.stream.BankCardMessage;
import com.platform.top.xiaoyu.run.service.api.finance.stream.FinanceOutSource;
import com.platform.top.xiaoyu.run.service.api.finance.stream.TypeMessage;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BankBindingVO;
import com.platform.top.xiaoyu.run.service.api.system.feign.IPlatformBankFeignClient;
import com.platform.top.xiaoyu.run.service.api.system.vo.PlatformBankModelVO;
import com.platform.top.xiaoyu.run.service.api.system.vo.req.PlatformBankQueryReq;
import com.platform.top.xiaoyu.run.service.finance.entity.BankBinding;
import com.platform.top.xiaoyu.run.service.finance.enums.BandingTypeEnums;
import com.platform.top.xiaoyu.run.service.finance.mapper.BankBindingMapper;
import com.platform.top.xiaoyu.run.service.finance.service.IBankBindingService;
import com.top.xiaoyu.rearend.component.mybatisplus.service.AbstractMybatisPlusService;
import com.top.xiaoyu.rearend.tool.api.R;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.bean.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Consumer;

/**
 * 绑卡 服务实现类
 *
 * @author coffey
 */
@Service
public class BankBindingServiceImpl extends AbstractMybatisPlusService<BankBindingMapper, BankBinding, Long> implements IBankBindingService {

	@Autowired
	private BankBindingMapper bankBindingMapper;
	@Autowired
    private IPlatformBankFeignClient iPlatformBankFeignClient;
	@Autowired
	private FinanceOutSource outSource;

	@Override
	public Page<BankBindingVO> findPage(Page<BankBindingVO> page, BankBindingVO vo) {
		if( null == vo || null == vo.getPlatformId() || vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return bankBindingMapper.findPage(page, vo);
	}
	@Value("${top.fastdfs.url}")
	public String fastDfsUrl;
	@Override
	public List<BankBindingVO> findListALL(BankBindingVO vo) {
		if( null == vo || null == vo.getPlatformId() || vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		List<BankBindingVO> listAll = bankBindingMapper.findListAll(vo);
		listAll.forEach(new Consumer<BankBindingVO>() {
			@Override
			public void accept(BankBindingVO bankBindingVO) {
				bankBindingVO.setUrlIcon( fastDfsUrl + "/"+ bankBindingVO.getUrlIcon());
			}
		});
		return listAll;
	}

	@Override
	public BankBindingVO findDetail(BankBindingVO bankBindingVO) {
		if( null == bankBindingVO || null == bankBindingVO.getPlatformId() || bankBindingVO.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		return bankBindingMapper.findDetail(bankBindingVO);
	}

	@Override
	public BankBindingVO findDetailId(Long id, Long platformId) {
		return findDetail(BankBindingVO.builder().id(id).platformId(platformId).build());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean insert(BankBindingVO bankBindingVO) {
		if( null == bankBindingVO || null == bankBindingVO.getPlatformId() || bankBindingVO.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		//绑卡类型 为 银行
		if (null != BandingTypeEnums.getType(bankBindingVO.getType())) {

            PlatformBankQueryReq req = new PlatformBankQueryReq();
            req.setBankCode(bankBindingVO.getBankCode());
            R rBank = iPlatformBankFeignClient.findList(req);
            if(null != rBank) {
                if ( !rBank.isSuccess() ) {
                    //未查询到银行卡数据
                    throw new BizBusinessException(BaseExceptionType.BANK_TYPE_NULL);
                }
            }

            List<PlatformBankModelVO> bankModelVOS =  (List<PlatformBankModelVO>)rBank.getData();
            if(CollectionUtils.isEmpty(bankModelVOS)) {
                //未查询到银行卡数据
                throw new BizBusinessException(BaseExceptionType.BANK_TYPE_NULL);
            }

            PlatformBankModelVO bankModelVO = bankModelVOS.get(0);

			//查询当前人同国家、相同银行类型code、相同的卡号 不能新增数据
			BankBindingVO carNo_findBinding = new BankBindingVO();
			//类型 1 银行卡
			carNo_findBinding.setType(bankBindingVO.getType());
			//所属银行code
			carNo_findBinding.setBankCode(bankModelVO.getBankCode());
			//银行卡号
			carNo_findBinding.setAccountNo(bankBindingVO.getAccountNo());
			carNo_findBinding.setUserId(bankBindingVO.getUserId());

			carNo_findBinding = bankBindingMapper.findDetail(carNo_findBinding);
			if ( null != carNo_findBinding ) {
				throw new BizBusinessException(BaseExceptionType.BANK_TYPE_TRUE);
			}

			bankBindingVO.setBankCode(bankModelVO.getBankCode());
			bankBindingVO.setBankName(bankModelVO.getBankName());
			bankBindingVO.setBankAddress(bankModelVO.getBankWebsite());
			bankBindingVO.setUrlBackroug(bankModelVO.getBackIcon());
			bankBindingVO.setUrlIcon(bankModelVO.getIcon());
		}

		bankBindingVO.setStatuss(CommonStatus.DISABLE.getValue());
		bankBindingVO.setCreateTimestamp(LocalDateTime.now());
		bankBindingVO.setIsDeleted(0);

		//查询绑卡记录是否已经存在
		BankBindingVO find_vo = bankBindingMapper.findDetail(BankBindingVO.builder().userId(bankBindingVO.getUserId()).platformId(bankBindingVO.getPlatformId()).build());
		if (null == find_vo) {
			//不存在状态码给默认
			bankBindingVO.setStatuss(CommonStatus.ENABLE.getValue());

			//发送消息给用户系统
			BankCardMessage bankCardName = new BankCardMessage();

			bankCardName.setUserId(bankBindingVO.getUserId());
			bankCardName.setRealName(bankBindingVO.getAccountName());
			bankCardName.setPlatformId(bankBindingVO.getPlatformId());
			outSource.financeToUserOutput().send(MessageBuilder.withPayload(bankCardName).setHeader(TypeMessage.MESSAGETYPE, TypeMessage.TYPE_BANKCARD_MSG).build());

		}
		return this.save(BeanCopyUtils.copyBean(bankBindingVO, BankBinding.class));

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateIsDefault(Long id, Long userId, Long platformId) {

		BankBindingVO udpate_default = new BankBindingVO();

		//查询默认绑卡记录
		udpate_default.setUserId(userId);
		udpate_default.setPlatformId(platformId);
		udpate_default = this.findDetail(udpate_default);
		udpate_default.setStatuss(CommonStatus.DISABLE.getValue());
		//更新成非默认
		this.updateVOById(udpate_default);

		BankBindingVO bankBindingVO = new BankBindingVO();
		bankBindingVO.setId(id);
		bankBindingVO.setUserId(userId);
		bankBindingVO.setPlatformId(platformId);
		bankBindingVO.setUpdateTimestamp(LocalDateTime.now());
		bankBindingVO.setStatuss(CommonStatus.ENABLE.getValue());
		//设置默认绑卡记录
		return this.updateVOById(bankBindingVO);

	}

	private boolean updateVOById(BankBindingVO vo) {
		if( null == vo || null == vo.getPlatformId() || vo.getPlatformId().longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		UpdateWrapper<BankBinding> updateWrapper = new UpdateWrapper<BankBinding>();
		updateWrapper.eq(BankBinding.PK_ID, vo.getId()).eq(BankBinding.P_PLATFORM_ID, vo.getPlatformId());
		return this.update(BeanCopyUtils.copyBean(vo, BankBinding.class), updateWrapper);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean del(List<Long> ids, Long platformId) {
		if( null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		QueryWrapper<BankBinding> queryWrapper = new QueryWrapper<BankBinding>();
		queryWrapper.in(BankBinding.PK_ID, ids).eq(BankBinding.P_PLATFORM_ID, platformId);
		return this.remove(queryWrapper);
	}

	/**
	 * 恢复逻辑删除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateEnable(Long id, Long platformId) {
		BankBindingVO vo = BankBindingVO.builder().id(id).platformId(platformId).build();
		vo.setStatuss(CommonStatus.ENABLE.getValue());
		return updateVOById(vo);
	}

	/**
	 * 作废， 逻辑删除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateCancal(Long id, Long platformId) {
		if( null == platformId || platformId.longValue() <= 0 ) {
			throw new BizBusinessException(BaseExceptionType.PARAM_PLATFORM);
		}
		//查询当前数据
		BankBindingVO bindingVO = this.findDetail(BankBindingVO.builder().id(id).platformId(platformId).build());

		if(null == bindingVO) {
			return false;
		}

		/* 当前数据行是默认 */
		boolean flag = false;
		if ( bindingVO.getStatuss().intValue() == CommonStatus.ENABLE.getValue().intValue() ) {
			flag = true;
		}
		//判断是否更新
		boolean ret_flag = true;

		//是默认值， 修改其他数据行为默认
		if(flag) {

			BankBindingVO find_bingdingVO = new BankBindingVO();
			find_bingdingVO.setUserId(bindingVO.getUserId());
			find_bingdingVO.setPlatformId(bindingVO.getPlatformId());
			//查询当前用户的所有绑定记录
			List<BankBindingVO> list = this.findListALL(find_bingdingVO);

			for (BankBindingVO vo : list) {
				//不等于当前数据行
				if (vo.getId().longValue() != id.longValue() ) {

					//不是默认值 && 不是已删除状态
					if(vo.getStatuss().intValue() == CommonStatus.DISABLE.getValue().intValue() && vo.getIsDeleted().intValue() ==  0 ) {

						BankBinding entity = new BankBinding();
						entity.setId(vo.getId());
						entity.setPlatformId(vo.getPlatformId());
						entity.setStatuss(CommonStatus.ENABLE.getValue());

						//修改当前数据为默认
						ret_flag = this.updateById(entity);

						break;
					}
				}
			}
		}

		if (ret_flag) {
			BankBindingVO vo = BankBindingVO.builder().id(id).platformId(platformId).build();
			vo.setStatuss(CommonStatus.DISABLE.getValue());
			return updateVOById(vo);
		}
		return false;
	}

}
