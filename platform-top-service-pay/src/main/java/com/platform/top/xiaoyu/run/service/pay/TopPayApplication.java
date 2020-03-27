package com.platform.top.xiaoyu.run.service.pay;

import com.platform.top.xiaoyu.run.common.constant.CommonConstant;
import com.platform.top.xiaoyu.run.common.enums.ServiceApplicationTypeStatus;
import com.platform.top.xiaoyu.run.service.api.common.constant.ApiConstant;
import com.platform.top.xiaoyu.run.service.pay.constant.Constant;
import com.top.xiaoyu.rearend.launch.TopLaunchApplication;
import com.top.xiaoyu.rearend.log.constant.TopLogConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.PlatformTransactionManager;

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
        TopLogConstant.LOG_FEIGN_FALLBACK_PACKAGE_PATH //日志
})
@ServletComponentScan(basePackages = {
	ApiConstant.FILTER_PACKAGE_PATH_SCAN
})
@MapperScan({Constant.MAPPER_SCAN_PATH})
public class TopPayApplication implements ApplicationRunner {

    @Bean
    public Object testBean(PlatformTransactionManager platformTransactionManager){
        System.out.println(">>>>>>>>>>" + platformTransactionManager.getClass().getName());
        return new Object();
    }

//	@Autowired
//	private IResourceFeginClient resourceFeginClient;

	public static void main(String[] args) {
		TopLaunchApplication.run(CommonConstant.APPLICATION_SERVICE_FS_PAY_NAME,
			new String[]{
				ServiceApplicationTypeStatus.APPLICATION_SERVICE_FS_PAY_NAME.getName()
			},
			TopPayApplication.class, args);
	}

	/**
	 * 上报授权服务资源
	 */
	@Override
	public void run(ApplicationArguments args) {
//		List<RpcCreateResourceReq> createResourceReqs = RegistServiceResourceUtil.scan("/top-service-pay", "com.platform.top.xiaoyu.run.service.pay.controller");
//		resourceFeginClient.updateServiceResources(createResourceReqs);
	}
}

