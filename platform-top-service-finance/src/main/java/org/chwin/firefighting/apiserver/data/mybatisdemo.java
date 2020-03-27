
package org.chwin.firefighting.apiserver.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
//import groovy.xml.XmlUtil;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.alibaba.fastjson.JSON;

import static org.chwin.firefighting.apiserver.data.MybatisUtil.getSqlSessionFactory;

@SuppressWarnings("rawtypes")
public class mybatisdemo {
	public static void main(String[] args) throws Exception {
		//com.alibaba.cloudapi.sdk.model.ApiResponse
		//		 org.apache.ibatis.annotations; mybatis 3.5 jar
	//	org.springframework.beans.factory.annotation.Value
	//	com.alibaba.fastjson.JSON
	//	 io.swagger.annotations.ApiOperation
	//、、	FireCloudLib.jar
	//	org.springframework.plugin.core.Plugin
	//	io.swagger.models.Swagger
		//springfox.documentation.spi.DocumentationType
		SqlSessionFactory sqlSessionFactory =MybatisUtil. getSqlSessionFactory();

		SqlSession session = sqlSessionFactory.openSession(true);
	//	api ��Ϊ[ openSession(boolean autoCommit) ]���ò���ֵ���������Ƹ� sqlSession �Ƿ��Զ��ύ��true��ʾ�Զ��ύ��false��ʾ���Զ��ύ[���޲εķ�������һ�£������Զ��ύ]

		MybatisMapperCls mapper = session.getMapper(MybatisMapperCls.class);

		// List li =mapper.queryall();



		List<Map> li = mapper.query("call sp1(2)");
		System.out.println(JSON.toJSONString(li, true));
		session.close();
		// = session.selectList(arg0);

	}


}
