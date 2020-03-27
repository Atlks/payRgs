
package org.chwin.firefighting.apiserver.data;

import com.alibaba.fastjson.JSON;
import ognl.OgnlException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class SqlQueryer {
	public static void main(String[] args) throws Exception {

		String sql = "call sp1(2)";
		sql="select *  from t_json";
		String rzt = exe(sql);
		System.out.println(rzt);

		// = session.selectList(arg0);

	}

	private static String exe(String sql) throws IOException, OgnlException {
		SqlSessionFactory sqlSessionFactory = MybatisUtil. getSqlSessionFactory();

		SqlSession session = sqlSessionFactory.openSession(true);
		//	api ��Ϊ[ openSession(boolean autoCommit) ]���ò���ֵ���������Ƹ� sqlSession �Ƿ��Զ��ύ��true��ʾ�Զ��ύ��false��ʾ���Զ��ύ[���޲εķ�������һ�£������Զ��ύ]

		MybatisMapperCls mapper = session.getMapper(MybatisMapperCls.class);

		// List li =mapper.queryall();


		List<Map> li = mapper.query(sql);
		session.close();
		return JSON.toJSONString(li, true);
	}


}
