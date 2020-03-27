package com.platform.top.xiaoyu.run.service.pay.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.top.xiaoyu.run.service.api.pay.vo.ToolsPayVO;
import com.platform.top.xiaoyu.run.service.pay.entity.ToolsPay;

import java.util.List;

/**
 * 用户支付接口等级积分配置
 *
 * @author coffey
 */
public interface IToolsPayService extends IService<ToolsPay> {

	/**
	 * 查询所有白名单
	 */
	public List<ToolsPayVO> findListAll(ToolsPayVO vo);

	/**
	 * 分页查询白名单
	 * @return
	 */
	public Page<ToolsPayVO> findPage(Page<ToolsPayVO> page, ToolsPayVO vo);

	public ToolsPayVO findDetail(Long id, Long platformId);

	public ToolsPayVO findDetail(ToolsPayVO vo);

	public ToolsPayVO findPriority(Long platformId);

	public boolean insert(ToolsPayVO req);

	public boolean update(ToolsPayVO req);

	public boolean delBatch(List<Long> ids, Long platformId);


}
