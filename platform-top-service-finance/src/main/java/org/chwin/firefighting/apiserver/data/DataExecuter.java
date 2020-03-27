package org.chwin.firefighting.apiserver.data;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ognl.OgnlException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.chwin.firefighting.apiserver.net.RequestImpAdv;
import org.chwin.firefighting.apiserver.net.RequestUtil;
import org.chwin.firefighting.apiserver.net.ResponseUtil;
import org.chwin.firefighting.apiserver.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@Api("数据操作api")
@Component
public class DataExecuter {
    @Autowired
    HttpServletRequest req;
    @Autowired
    HttpServletResponse res;
    public static void main(String[] args) throws Exception {
        System.out.println("--");
      //  pagetest();
        RequestImpAdv ri = new RequestImpAdv();
//        Map m= new HashMap(){{
//            this.put("type","故障".split(","));
//        }};
//        ri.map2=m;
     //   ri.getParameterMap();
      //  ri.getParameterValues("type");
       ri.setParam("type","故障");

        DataExecuter dataExecuter1 = new DataExecuter();
        dataExecuter1.req = ri;
//        dataContrler.res = resI;
        System.out.println(dataExecuter1.selectList("warning_query",1,2));

    }


    //  http://localhost:8088/selectList?select_id=warning_query&pagenum=1&pagesize=2
    @ApiOperation(value = "执行查询操作 通过接口", notes = "请求数据说明note")
    @RequestMapping(value = "/selectList")
    @ResponseBody
    public String selectList(String select_id,int pagenum,int pagesize) throws Exception {
        ResponseUtil.setAccessControlAllowOrigin(res);//cookie cross domain
        try {
            Map paramMap= RequestUtil.getMap(req);
            PageInfo pageInfo=  MybatisUtil.selectList(select_id,paramMap,pagenum,pagesize);
            return (JSON.toJSONString(pageInfo,true));

        } catch (Exception e) {
            return ExceptionUtil. getExceptionJson(e);
        }

    }



    private static void pagetest() throws IOException, OgnlException {
        String sql_id = "warning_query";
        SqlSessionFactory sqlSessionFactory = MybatisUtil. getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession(true);
        //	api ��Ϊ[ openSession(boolean autoCommit) ]���ò���ֵ���������Ƹ� sqlSession �Ƿ��Զ��ύ��true��ʾ�Զ��ύ��false��ʾ���Զ��ύ[���޲εķ�������һ�£������Զ��ύ]

        //设置分页条件，Parameters:pageNum 页码pageSize 每页显示数量count 是否进行count查询
        PageHelper.startPage(1, 3, true);
        // PageHelper.startPage(1,10);
//此时已经分页了
        List<Map> rzt_list = session.selectList(sql_id,null);

        //  可以使用PageInfo 查看分页信息
        PageInfo pageInfo = new PageInfo<>(rzt_list);
        System.out.println(JSON.toJSONString(pageInfo));
        System.out.println(JSON.toJSONString(rzt_list, true));
    }


}
