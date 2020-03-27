package org.chwin.firefighting.apiserver.data;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import groovy.xml.XmlUtil;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;
import java.util.List;
import java.util.Map;


public class MybatisUtil4game {

    public static void main(String[] args) throws Exception {

        //System.out.println( new MybatisUtil());
//       Map dbcfg=SpringUtil.getDbcfg();
//         SqlSessionFactory sqlSessionFactory =MybatisUtil. getSqlSessionFactory();
//        System.out.println(dbcfg);

        Map m= Maps.newLinkedHashMap();
        m.put("user_id","204963779627192320");
        System.out.println( MybatisUtil4game.selectList("recharge_count",m));
    }

    public static SqlSessionFactory getSqlSessionFactory() throws IOException, OgnlException {
        String mybatisCfg_result = getCfgTxt();


      //  System.out.println(mybatisCfg_result);
        InputStream is2=new ByteArrayInputStream(mybatisCfg_result.getBytes());
        // ����sqlSession �Ĺ���
        return new SqlSessionFactoryBuilder().build(is2);
    }

    public static String getCfgTxt() throws IOException, OgnlException {
        String resource = "d:/mybatis.xml";
        // ����mybatis �������ļ�����Ҳ���ع�����ӳ���ļ���
        //ClassLoader classLoader = .getClassLoader();
        InputStream is =new FileInputStream(resource);
                //mybatisdemo.class.getResourceAsStream(resource);
        String mybatisCfg_result = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));

 //       String getCfgFile=SpringUtil.getCfgFile();
//        org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
//        Object mObject=yaml.load(mybatisdemo.class.getResourceAsStream(getCfgFile));
//        Object expression = Ognl.parseExpression("spring.datasource.url");
//        Object url = Ognl.getValue(expression, mObject);
//        Object usr = Ognl.getValue(Ognl.parseExpression("spring.datasource.username"), mObject);
//        Object pwd = Ognl.getValue(Ognl.parseExpression("spring.datasource.password"), mObject);
     //   if(pwd==null)pwd="";
     //   url= XmlUtil.escapeXml(url.toString()+"&allowMultiQueries=true");

        return mybatisCfg_result;
    }


    public static PageInfo executeQuery(String select_id) throws IOException, OgnlException {
        SqlSessionFactory sqlSessionFactory = MybatisUtil4game. getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession(true);
        //	api ��Ϊ[ openSession(boolean autoCommit) ]���ò���ֵ���������Ƹ� sqlSession �Ƿ��Զ��ύ��true��ʾ�Զ��ύ��false��ʾ���Զ��ύ[���޲εķ�������һ�£������Զ��ύ]

        //设置分页条件，Parameters:pageNum 页码pageSize 每页显示数量count 是否进行count查询
        PageHelper.startPage(1, 10, true);
        // PageHelper.startPage(1,10);
//此时已经分页了
        List<Map> rzt_list = session.selectList(select_id,null);

        //  可以使用PageInfo 查看分页信息
        PageInfo pageInfo = new PageInfo<>(rzt_list);
return pageInfo;
    }

    public static PageInfo executeQuery(String select_id, Map paramMap) throws IOException, OgnlException {
        SqlSessionFactory sqlSessionFactory = MybatisUtil4game. getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession(true);
        //	api ��Ϊ[ openSession(boolean autoCommit) ]���ò���ֵ���������Ƹ� sqlSession �Ƿ��Զ��ύ��true��ʾ�Զ��ύ��false��ʾ���Զ��ύ[���޲εķ�������һ�£������Զ��ύ]

        //设置分页条件，Parameters:pageNum 页码pageSize 每页显示数量count 是否进行count查询
        PageHelper.startPage(1, 10, true);
        // PageHelper.startPage(1,10);
//此时已经分页了
        List<Map> rzt_list = session.selectList(select_id,paramMap);

        //  可以使用PageInfo 查看分页信息
        PageInfo pageInfo = new PageInfo<>(rzt_list);
        return pageInfo;

    }


    public static PageInfo selectList(String select_id, Map paramMap,int pageNum,int pagesize) throws IOException, OgnlException {
        SqlSessionFactory sqlSessionFactory = MybatisUtil4game. getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession(true);
        //	api ��Ϊ[ openSession(boolean autoCommit) ]���ò���ֵ���������Ƹ� sqlSession �Ƿ��Զ��ύ��true��ʾ�Զ��ύ��false��ʾ���Զ��ύ[���޲εķ�������һ�£������Զ��ύ]

        //设置分页条件，Parameters:pageNum 页码pageSize 每页显示数量count 是否进行count查询
        PageHelper.startPage(pageNum, pagesize, true);
        // PageHelper.startPage(1,10);
//此时已经分页了
        List<Map> rzt_list = session.selectList(select_id,paramMap);

        //  可以使用PageInfo 查看分页信息
        PageInfo pageInfo = new PageInfo<>(rzt_list);
        return pageInfo;

    }

    public static SqlSession getSqlSession()  throws IOException, OgnlException {
        SqlSessionFactory sqlSessionFactory = MybatisUtil4game. getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession(true);


        return session;

    }
    public static List selectList(String select_id, Map paramMap) throws IOException, OgnlException {
        SqlSessionFactory sqlSessionFactory = MybatisUtil4game. getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession(true);
        //	api ��Ϊ[ openSession(boolean autoCommit) ]���ò���ֵ���������Ƹ� sqlSession �Ƿ��Զ��ύ��true��ʾ�Զ��ύ��false��ʾ���Զ��ύ[���޲εķ�������һ�£������Զ��ύ]


        List<Map> rzt_list = session.selectList(select_id,paramMap);


        return rzt_list;

    }
}
