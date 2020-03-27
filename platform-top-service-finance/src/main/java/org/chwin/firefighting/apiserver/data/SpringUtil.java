package org.chwin.firefighting.apiserver.data;

import com.google.common.collect.Maps;
import com.platform.top.xiaoyu.run.service.api.common.constant.ApiConstant;
import com.platform.top.xiaoyu.run.service.finance.controller.backstage.BankBindingController;
import com.top.xiaoyu.rearend.log.constant.TopLogConstant;
import ognl.Ognl;
import ognl.OgnlException;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;


@ComponentScan(basePackages = {
	ApiConstant.BASE_PACKAGES,
	ApiConstant.BASE_PLATFORM_PACKAGES
})

@SpringBootApplication

public class SpringUtil {

	public static void main(String[] args) {
		//启动WEB项目
		Class<?> aClass = BankBindingController.class;


		Object bean = getBean(aClass);
		System.out.println(bean);

	//	System.out.println(context.getBean( "com.platform.top.xiaoyu.run.service.finance.controller.backstage.BankBindingController")  );

//		UserPoJpaRepository userPoJpaRepository = context.getBean(UserPoJpaRepository.class);
	}

	@NotNull
	public static Object getBean( Class<?> aClass) {
		SpringApplication application = new SpringApplication(SpringUtil.class);
		ConfigurableApplicationContext context = application.run();

		return context.getBean(aClass);
	}

	public static String getCfgFile()
    {
        try{
            String profiles_active=  get_profiles_active();
            return "/application-"+profiles_active+".yml";
        }catch( OgnlException e)
        {
            return "/application.yml";
        }
    }


    // cfgTag  dev prod
    public static String get_profiles_active() throws OgnlException {
        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
        Object mObject=yaml.load(mybatisdemo.class.getResourceAsStream("/application.yml"));
        Object expression = Ognl.parseExpression("spring.profiles.active");
        Object url = Ognl.getValue(expression, mObject);
        return url.toString();
    }

    public static Map getDbcfg() throws  Exception
    {
        String cfgTag=get_profiles_active();
        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
        Object mObject=yaml.load(mybatisdemo.class.getResourceAsStream("/application-"+cfgTag+".yml"));
        Object expression = Ognl.parseExpression("spring.datasource.url");
        Object url = Ognl.getValue(expression, mObject);
        Object usr = Ognl.getValue(Ognl.parseExpression("spring.datasource.username"), mObject);
        Object pwd = Ognl.getValue(Ognl.parseExpression("spring.datasource.password"), mObject);
        if(pwd==null)pwd="";
        Map m= Maps.newLinkedHashMap();
        m.put("url",url);
        m.put("user",usr.toString());
        m.put("pwd",pwd.toString());
        return m;
    }
}
