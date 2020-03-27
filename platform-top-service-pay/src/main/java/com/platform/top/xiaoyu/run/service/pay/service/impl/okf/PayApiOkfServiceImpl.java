package com.platform.top.xiaoyu.run.service.pay.service.impl.okf;

import com.alibaba.fastjson.JSON;
import com.platform.top.xiaoyu.run.service.api.finance.exception.BaseExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.exception.PayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.utils.PaySignUtil;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.okf.OkfPayResp;
import com.platform.top.xiaoyu.run.service.pay.service.IPayService;
import com.platform.top.xiaoyu.run.service.pay.service.okf.IPayApiOkfService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 芒果支付系统对接 服务实现类
 * @author coffey
 */
@Service
@Slf4j
public class PayApiOkfServiceImpl implements IPayApiOkfService {

	@Value("${pay.sysPath}")
	private String sysPath;
	@Value("${pay.okf.rootPath}")
	private String rootPath;

	@Value("${pay.okf.merchantId}")
	private String merchantId;
	@Value("${pay.okf.signKey}")
	private String signKey;

	@Value("${pay.okf.notifyUrl}")
	private String notifyUrl;
	@Value("${pay.okf.returnUrl}")
	private String returnUrl;

	@Value("${pay.okf.url.pay}")
	private String urlPay;

	@Autowired
	private IPayService payService;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public OkfPayResp payOkfPost(PayReq payReq, String money, String payType) {

		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("appid", merchantId);
		reqMap.put("appkey", signKey);
		reqMap.put("orderid", payReq.getOrderNo());
		reqMap.put("money", money);
		// 平台每个会员分配的接口编码 微信:1, 支付宝:2, QQ:3，银行卡:4
		reqMap.put("paycode", payType);
		reqMap.put("notifyurl", sysPath + notifyUrl);
		reqMap.put("returnurl", sysPath + returnUrl);
		reqMap.put("goodsname", payReq.getList().get(0).getName());
		reqMap.put("remark", payReq.getParam());
		//超时时间 5分钟
		reqMap.put("orderperiod", "5");
		reqMap.put("membername", payReq.getPayAccount());
		reqMap.put("timestamp", String.valueOf(System.currentTimeMillis()) );

		String signMd5 = PaySignUtil.sign(reqMap);

		reqMap.remove("appkey");
		reqMap.put("sign", signMd5);
		reqMap.put("sign_type", "MD5");

		log.info("请求OKF平台 http ==》"+reqMap.toString());
		OkfPayResp resp = OkHttpClient.post(rootPath + urlPay).param(reqMap).asBean(OkfPayResp.class);

//		if(null != resp) {
//			if(!resp.getState()) {
//				throw new BizBusinessException(resp.getMsg());
//			}
//		}

		return resp;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean notify(String data) {
		try {
			log.info("开始解析OKF回调");

			Map<String, String> map = this.getMap(data);

			if(null == map) {
				throw new BizBusinessException(BaseExceptionType.DATA_NULL);
			}

			//			map 参数如下：
//			1.	appid	商户号 APPID	Y	平台分配的唯一商户编号跟请求一致
//			2.	orderid	商户订单号	Y	跟请求一致
//			3.	transorder	平台交易单号	Y	交易成功后平台返回的交易单号
//			4.	money	商户订单金额	Y	跟请求一致
//			5.	realmoney	实付金额	Y	订单实际支付金额
//			6.	paycode	支付方式	Y	跟请求一致
//			7.	status	支付状态	Y	返回1成功 2失败
//			8.	goodsname	商品名称	Y	跟请求一致
//			9.	remark	自定义参数	N	跟请求一致
//			10.	timestamp	订单Unix时间戳	Y	订单Unix时间戳
//			11.	sign	签名	Y	签名信息与提交接口的加密方式一样。（详见最下面签名加密说明）

			log.info("请求回调到财务系统");
			if( !payService.callPay(Long.parseLong(map.get("orderid")), map.get("realmoney"), map.toString(), map.get("remark")) ) {
				throw new BizBusinessException(PayExceptionType.PAY_CALL_FAIL);
			}
			return true;
		} catch (Exception ex) {
			throw new BizBusinessException(BaseExceptionType.RPC_CALL_NOTIFY);
		}

	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void returnhtml(String data) {
		try {
			log.info("开始解析OKF回调");

			Map<String, String> map = this.getMap(data);

			if(null == map) {
				throw new BizBusinessException(BaseExceptionType.DATA_NULL);
			}

			log.info("发起请求返回页面");
//			map 参数如下：
//			1.	appid	商户号 APPID	Y	平台分配的唯一商户编号跟请求一致
//			2.	orderid	商户订单号	Y	跟请求一致
//			3.	transorder	平台交易单号	Y	交易成功后平台返回的交易单号
//			4.	money	商户订单金额	Y	跟请求一致
//			5.	realmoney	实付金额	Y	订单实际支付金额
//			6.	paycode	支付方式	Y	跟请求一致
//			7.	status	支付状态	Y	返回1成功 2失败
//			8.	goodsname	商品名称	Y	跟请求一致
//			9.	remark	自定义参数	N	跟请求一致
//			10.	timestamp	订单Unix时间戳	Y	订单Unix时间戳
//			11.	sign	签名	Y	签名信息与提交接口的加密方式一样。（详见最下面签名加密说明）

			log.info("请求回调到财务系统 html");
			if( !payService.callPay(Long.parseLong(map.get("orderid")), map.get("realmoney"), map.toString(), map.get("remark")) ) {
				throw new BizBusinessException(PayExceptionType.PAY_CALL_FAIL);
			}
		} catch (Exception ex) {
			throw new BizBusinessException(BaseExceptionType.RPC_CALL_NOTIFY);
		}
	}

	private Map<String, String> getMap(String data) {
		try {
			String jsonStr = new String(Base64.getDecoder().decode(data), "UTF-8");
			log.info("开始解析" + jsonStr);
			Map<String, Object> params = JSON.parseObject(jsonStr);
			Map<String, String> map = new HashMap<String, String>();

			for (String obj : params.keySet()) {
				map.put(obj, params.get(obj).toString());
			}
			map.put("appkey", signKey);

			String inSign = map.get("sign");
			map.remove("sign");

			//签名
			String mySign = PaySignUtil.sign(map);
			if (!inSign.equals(mySign)) {
				log.info("OKF解析签名失败");
				throw new BizBusinessException(PayExceptionType.PAY_OKF_CALL_FAIL);
			}
			return map;
		} catch (Exception ex) {
			throw new BizBusinessException(BaseExceptionType.RPC_CALL_NOTIFY);
		}
	}
}
