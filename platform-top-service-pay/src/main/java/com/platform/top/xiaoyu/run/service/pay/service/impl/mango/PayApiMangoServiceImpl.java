package com.platform.top.xiaoyu.run.service.pay.service.impl.mango;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.platform.top.xiaoyu.run.service.api.pay.enums.PayTypeMangoEnums;
import com.platform.top.xiaoyu.run.service.api.pay.exception.PayExceptionType;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.mango.MangoCallReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayProductData;
import com.platform.top.xiaoyu.run.service.api.pay.vo.req.pay.PayReq;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.mango.FindPayResp;
import com.platform.top.xiaoyu.run.service.api.pay.vo.resp.mango.MangoPayResp;
import com.platform.top.xiaoyu.run.service.pay.service.IPayService;
import com.platform.top.xiaoyu.run.service.pay.service.mango.IPayApiMangoService;
import com.top.xiaoyu.rearend.tool.exception.BizBusinessException;
import com.top.xiaoyu.rearend.tool.util.date.DateUtil;
import com.top.xiaoyu.rearend.tool.util.digest.md5.MD5Util;
import com.top.xiaoyu.rearend.tool.util.okhttp.OkHttpClient;
import com.top.xiaoyu.rearend.tool.util.okhttp.request.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 芒果支付系统对接 服务实现类
 *
 * @author coffey
 */
@Service
@Slf4j
public class PayApiMangoServiceImpl implements IPayApiMangoService {

	@Value("${pay.sysPath}")
	private String sysPath;

	@Value("${pay.mango.rootPath}")
	private String rootPath;

	@Value("${pay.mango.merchantId}")
	private String merchantId;
	@Value("${pay.mango.signKey}")
	private String signKey;
	@Value("${pay.mango.form}")
	private String form;

	@Value("${pay.mango.notifyUrl}")
	private String notifyUrl;
	@Value("${pay.mango.returnUrl}")
	private String returnUrl;

	@Value("${pay.mango.url.findPay}")
	private String urlFindPay;
	@Value("${pay.mango.url.findPayDf}")
	private String urlFindPayDf;
	@Value("${pay.mango.url.pay}")
	private String urlPay;
	@Value("${pay.mango.url.payDf}")
	private String urlPayDf;

	@Autowired
	private IPayService payService;

	/**
	 * 调用芒果的支付接口
	 * @param req
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public MangoPayResp payMangoPost(PayReq req, String money, String payType) {

//		963 银联扫码/云闪付
//		972 支付宝
//		975 支付宝转卡 扫码方式
//		985 微信转卡
//		986 zfb转卡 跳转页面
		if(StringUtils.isEmpty(req.getPayBankCode())) {
			throw new BizBusinessException(PayExceptionType.PAY_PARAM_BANKCODE);
		}
		if(!PayTypeMangoEnums.contain(req.getPayBankCode())) {
			throw new BizBusinessException(PayExceptionType.PAY_PARAM_BANKCODE_FAIL);
		}

		String    pay_amount_param = "pay_amount";
		String    pay_amount = money;
		String    pay_applydate_param = "pay_applydate";
		//提交时间 yyyy-MM-dd HH:mm:ss
		String    pay_applydate = DateUtil.format(new Date(), DateUtil.PATTERN_DATETIME);
		String    pay_bankcode_param = "pay_bankcode";
		String    pay_bankcode = payType;
		String    pay_callbackurl_param = "pay_callbackurl";
		//回调地址
		String    pay_callbackurl = sysPath + returnUrl ;
		String    pay_memberid_param = "pay_memberid";
		String    pay_notifyurl_param = "pay_notifyurl";
		//通知地址
		String    pay_notifyurl = sysPath + notifyUrl ;
		String    pay_orderid_param = "pay_orderid";
		//20位支付单号 时间戳+6位随机字符串组成
		String    pay_orderid = req.getOrderNo()+"";

		//A-Z a-z 参数首字母排序
		String stringSignTemp=
				pay_amount_param + "=" + pay_amount +
				"&" + pay_applydate_param + "=" + pay_applydate +
				"&" + pay_bankcode_param + "=" + pay_bankcode +
				"&" + pay_callbackurl_param + "=" + pay_callbackurl +
				"&" + pay_memberid_param + "=" + merchantId +
				"&" + pay_notifyurl_param + "=" + pay_notifyurl +
				"&" + pay_orderid_param + "=" + pay_orderid +
				"&key=" + signKey;

		//加密
		String pay_md5sign = MD5Util.string2MD5HexUpper(stringSignTemp);

		PayProductData productReq = req.getList().get(0);

		MangoPayResp resp = OkHttpClient
			.post(rootPath + urlPay)
			.param(pay_memberid_param, merchantId)
			.param(pay_orderid_param, pay_orderid)
			.param(pay_applydate_param, pay_applydate)
			.param(pay_bankcode_param, pay_bankcode)
			.param(pay_notifyurl_param, pay_notifyurl)
			.param(pay_callbackurl_param, pay_callbackurl)
			.param(pay_amount_param, pay_amount)
			.param("pay_md5sign", pay_md5sign)
			.param("pay_productname", productReq.getName())
			.param("pay_productnum", productReq.getNum()+"")
			.param("pay_productdesc", productReq.getDesc())
			.param("pay_producturl", productReq.getPlatformUrl())
			.param("pay_attach", String.valueOf(req.getPlatformId()))
			.param("from", form)
//			.header("pay_attach", "customValue1")//自定义header
			.asBean(MangoPayResp.class);


		if(!resp.getStatus().equals("1")) {
			throw new BizBusinessException(resp.getMsg());
		}

		return resp;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean notify(MangoCallReq req) {

		//1 验证sign 签名
		String stringSignTemp = "amount=" + Double.parseDouble(req.getAmount()) +
				"&attach=" + req.getAttach() +
			    "&datetime=" + req.getDatetime() +
				"&memberid=" + merchantId +
				"&orderid=" + req.getOrderid() +
				"&returncode=" + req.getReturncode() +
				"&transaction_id=" + req.getTransaction_id() +
				"&key=" + signKey;
		//加密
		String pay_md5sign = MD5Util.string2MD5HexUpper(stringSignTemp);
		//请求验证sign
		if( !req.getSign().equals(pay_md5sign) ) {
			throw new BizBusinessException(PayExceptionType.PAY_MANGO_CALL_FAIL_SIGN);
		}

		//回调支付结果不成功
		if( !req.getReturncode().equals("00")) {
			log.info("mango支付平台 通知回调接口失败");
			throw new BizBusinessException(PayExceptionType.PAY_MANGO_CALL_FAIL);
		}

		//3 回调出口总方法，写入支付流水
		if( !payService.callPay(Long.parseLong(req.getOrderid()), req.getAmount(), req.toString(), req.getAttach()) ) {
			throw new BizBusinessException(PayExceptionType.PAY_CALL_FAIL);
		}

		return true;
	}

	/**
	 * 代付接口
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void payMangoDfPost() {
//		String out_trade_no = "orderid01";    //商户订单号	是	是	保证唯一值
//		String money = "10";  	     //订单金额	是	是	单位：元
//		String bankname = "工商银行";        //开户行名称	是	是
//		String subbranch = "工商银行广州分行";       //支行名称	是	是
//		String accountname = "张三";     //开户名	是	是
//		String cardnumber = "213123123";      //银行卡号	是	是
//		String province = "3213";        //省份	是	是
//		String city = "42123";            //城市	是	是
//		String extendsJson = "";      //附加字段	否	是  格式：先转化为JSON，接着进行Base64加密
//
//		PayDfResp ret = OkHttpClient
//			.post(root_path + url_req_pay_df)
//			.param("mchid", merchantId)
//			.param("out_trade_no", out_trade_no)
//			.param("money", money)
//			.param("bankname", bankname)
//			.param("subbranch", subbranch)
//			.param("accountname", accountname)
//			.param("cardnumber", cardnumber)
//			.param("province", province)
//			.param("city", city)
//			.param("extends", extendsJson)
//			.param("pay_md5sign", sign_md5)
//			.asBean(PayDfResp.class);
//		System.out.println("apiResult = " + ret);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void payMangoDfPostCall() {

	}

	@Override
	public FindPayResp findPayDetail(String pay_orderid) {

		String pay_memberid_param = "pay_memberid";
		String pay_orderid_param = "pay_orderid";

		//验证sign 签名
		String stringSignTemp= pay_memberid_param +"="+ merchantId +
			"&" + pay_orderid_param + "=" + pay_orderid +
			"&key=" + signKey;
		//加密
		String pay_md5sign = MD5Util.string2MD5HexUpper(stringSignTemp);

		String param = pay_memberid_param + "=" + merchantId
			+ "&" + pay_orderid_param + "=" + pay_orderid
			+ "&pay_md5sign=" + pay_md5sign
			;

		ByteArrayInputStream bais = new ByteArrayInputStream(
			param.getBytes(Charset.forName("UTF-8")));

		FindPayResp ret = OkHttpClient
			.binaryBody(rootPath + urlFindPay)
			.contentType(ContentType.APPLICATION_FORM_URLENCODED)
			.stream(bais)
			.asBean(FindPayResp.class);

		System.out.println("apiResult = " + ret);

		if ( null == ret || !ret.getReturncode().equals("00") ) {
//			throw new BizBusinessException();

			throw new BizBusinessException(PayExceptionType.PAY_MANGO_FIND_FAIL, ret.toString());
		}

		return ret;
	}

	@Override
	public void findDfPayDetail() {

	}

	@Override
	public List<Map<Integer, String>> findBankCode() {
		List<Map<Integer, String>> list = Lists.newArrayList();
		for (PayTypeMangoEnums value : PayTypeMangoEnums.values()) {
			Map<Integer, String> m  = Maps.newHashMap();
			m.put(value.getValue(), value.getDesc());
			list.add(m);
		}

		return list;
	}


//	public static void main(String[] args) {
//		String    pay_amount_param = "pay_amount";
//		String    pay_amount = 110+"";
//		String    pay_applydate_param = "pay_applydate";
//		String    pay_applydate = DateUtil.format(new Date(), DateUtil.PATTERN_DATETIME);;  //提交时间 yyyy-MM-dd HH:mm:ss
//		String    pay_bankcode_param = "pay_bankcode";
//		String    pay_bankcode = "975";
//		String    pay_callbackurl_param = "pay_callbackurl";
//		String    pay_callbackurl = "http://www.baidu.com"; //回调地址
//		String    pay_memberid_param = "pay_memberid";
//		String    pay_memberid = "180778887";//商户id
//		String    pay_notifyurl_param = "pay_notifyurl";
//		String    pay_notifyurl = "dddd" ;  //通知地址
//		String    pay_orderid_param = "pay_orderid";
//		String    pay_orderid = "31234567893214569875"; //20位订单号 时间戳+6位随机字符串组成
//
//		//A-Z a-z 参数首字母排序
//		String stringSignTemp=
//			pay_amount_param + "=" + pay_amount +
//				"&" + pay_applydate_param + "=" + pay_applydate +
//				"&" + pay_bankcode_param + "=" + pay_bankcode +
//				"&" + pay_callbackurl_param + "=" + pay_callbackurl +
//				"&" + pay_memberid_param + "=" + pay_memberid +
//				"&" + pay_notifyurl_param + "=" + pay_notifyurl +
//				"&" + pay_orderid_param + "=" + pay_orderid +
//				"&key=" + "hklpt8ely60t4nqa2w4k84uq06b1pd5v";
//
//		//加密
//		String pay_md5sign = MD5Util.string2MD5HexUpper(stringSignTemp);
//
//
//		MangoPayResp ret = OkHttpClient
//			.post("http://192.168.0.3:12100/Pay_Index.html")
//			.param(pay_memberid_param, pay_memberid)
//			.param(pay_orderid_param, pay_orderid)
//			.param(pay_applydate_param, pay_applydate)
//			.param(pay_bankcode_param, pay_bankcode)
//			.param(pay_notifyurl_param, pay_notifyurl)
//			.param(pay_callbackurl_param, pay_callbackurl)
//			.param(pay_amount_param, pay_amount)
//			.param("pay_md5sign", pay_md5sign)
//			.param("pay_productname", "aa")
//			.param("pay_productnum", "1")
//			.param("pay_productdesc","11")
//			.param("pay_producturl", "222")
//			.param("pay_attach", "333")
//			.param("from", "3")
////			.header("pay_attach", "customValue1")//自定义header
//			.asBean(MangoPayResp.class);
//		System.out.println("apiResult = " + ret);
//	}


}
