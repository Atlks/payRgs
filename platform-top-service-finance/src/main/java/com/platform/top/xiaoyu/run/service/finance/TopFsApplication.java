package com.platform.top.xiaoyu.run.service.finance;

import com.platform.top.xiaoyu.run.common.constant.CommonConstant;
import com.platform.top.xiaoyu.run.service.api.common.constant.ApiConstant;
import com.platform.top.xiaoyu.run.service.api.finance.stream.FinanceOutSource;
import com.platform.top.xiaoyu.run.service.api.finance.vo.BankBindingVO;
import com.platform.top.xiaoyu.run.service.finance.constant.Constant;
import com.platform.top.xiaoyu.run.service.finance.service.impl.BalanceServiceImpl;
import com.platform.top.xiaoyu.run.service.finance.stream.FinanceSink;
import com.top.xiaoyu.rearend.launch.TopLaunchApplication;
import com.top.xiaoyu.rearend.log.constant.TopLogConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 字段模块启动器
 *
 * @author coffey
 */
@SpringCloudApplication
@EnableFeignClients(basePackages = {
		ApiConstant.BASE_PACKAGES,
		ApiConstant.BASE_PLATFORM_PACKAGES,
		TopLogConstant.LOG_FEIGN_PACKAGE_PATH //日志
})
@ComponentScan(basePackages = {
		ApiConstant.BASE_PACKAGES,
		ApiConstant.BASE_PLATFORM_PACKAGES,
		TopLogConstant.LOG_FEIGN_FALLBACK_PACKAGE_PATH ,//日志
		"com.platform.top.xiaoyu.run.service.finance.stream"
})

@ServletComponentScan(basePackages = {
		ApiConstant.FILTER_PACKAGE_PATH_SCAN
})
@MapperScan({Constant.MAPPER_SCAN_PATH})
@EnableBinding({FinanceOutSource.class, FinanceSink.class})
public class TopFsApplication implements ApplicationRunner {

    @Bean
    public Object testBean(PlatformTransactionManager platformTransactionManager){
        System.out.println(">>>>>>>>>>" + platformTransactionManager.getClass().getName());
        return new Object();
    }

	@Value("${top.fastdfs.url}")
	public String fastDfsUrl;

	public static String fastDfsUrl2;
	@Value("${spring.profiles.active}")
	private String spring_profiles_active;
	public static void main(String[] args) {
	//	System.out.println(javax.servlet.http.MappingMatch.class);
	//

		System.out.println("aa");
	//	org.yaml.snakeyaml
//	 org.yaml.snakeyaml.
		System.out.println( System.getProperty("spring.profiles.activ"));
		TopLaunchApplication.run(CommonConstant.APPLICATION_SERVICE_FS_BOOK_NAME, TopFsApplication.class, args);
	}

//	@Autowired
//	private IResourceFeginClient resourceFeginClient;

	@Autowired
	BalanceServiceImpl BalanceServiceImpl1;
	/**
	 * 上报授权服务资源
	 */
	@Override
	public void run(ApplicationArguments args) {
		fastDfsUrl2=fastDfsUrl;
//		BankBindingVO.fastDfsUrl2=fastDfsUrl;
		System.out.println("______________=================******************atti::"+spring_profiles_active );
		Timer tmr=new Timer();
		tmr.schedule(new TimerTask() {
			@Override
			public void run() {
//				if(new Date().getHours()>23)
//				BalanceServiceImpl1.sumPplSumBalanceDateGrupbyPlat();

			}
		},0,300*1000);

//		ConfigurableApplicationContext applicationContext = SpringApplication.run(TopFsApplication.class, new String[]{});
//		System.out.println("2-2. 通过ApplicationContext获取Environment后再获取值: " + applicationContext
//				.getEnvironment().getProperty("top.fastdfs.url", "notcant"));
 	//System.exit(0);
//    List<RpcCreateResourceReq> createResourceReqs = RegistServiceResourceUtil.scan("/top-service-finance", "com.platform.top.xiaoyu.run.service.finance.controller");
//    	resourceFeginClient.updateServiceResources(createResourceReqs);
	}
}
