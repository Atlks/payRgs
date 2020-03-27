package org.chwin.firefighting.apiserver.data;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ognl.OgnlException;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.velocity.VelocityContext;
import org.chwin.firefighting.apiserver.dsl.velocityUtil;
import org.chwin.firefighting.apiserver.net.RequestUtil;
import org.chwin.firefighting.apiserver.net.ResponseUtil;
import org.chwin.firefighting.apiserver.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api("数据查询api")
@Controller
public class DataContrler {

    public static void main(String[] args) throws Exception {
        System.out.println();

      //  RequestImpAdv ri = new RequestImpAdv();

     //   System.out.println( new DataContrler().executeQueryMybatis("warning_query"));

//        RequestImpAdv ri = new RequestImpAdv();
//        ri.cookies_li.add(new Cookie("curUserInfo.uname", "新区维保"));
//        ri.cookies_li.add(new Cookie("curUserInfo.usertype", "维保公司"));
//        ri.setParam("sp", "sp1(99,@当前用户信息@)");
//        responseImp resI = new responseImp();
//
//        DataContrler dataContrler = new DataContrler();
//
//        dataContrler.req = ri;
//        dataContrler.res = resI;
//        dataContrler.execute(ri, resI);

    }

    @Autowired
    HttpServletRequest req;
    @Autowired
    HttpServletResponse res;

//  http://localhost:8088/executeQuery?select_id=warning_query
    @ApiOperation(value = "执行查询操作 通过接口", notes = "请求数据说明note")
    @RequestMapping(value = "/executeQuery")
    @ResponseBody
    public String executeQuery(String select_id) throws Exception {
        ResponseUtil.setAccessControlAllowOrigin(res);//cookie cross domain
   try {
       Map paramMap= RequestUtil.getMap(req);
            PageInfo pageInfo=  MybatisUtil.executeQuery(select_id,paramMap);
            return (JSON.toJSONString(pageInfo,true));

        } catch (Exception e) {
            return ExceptionUtil. getExceptionJson(e);
        }

    }



//throw new RuntimeException("not imp");
    //    com.github.pagehelper.PageInterceptor

    //   /execute?sp=sp1(33)
    @ApiOperation(value = "execute请求调用示例", notes = "invokePost说明")
    @RequestMapping(value = "/execute")
    @ResponseBody
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        res.setHeader("Access-Control-Allow-Origin", "*");
        try {
            if (req.getParameter("sp") != null) {
                //spmode
                return executeSp(req.getParameter("sp"), res);

            }
            if (req.getParameter("view") != null) {
                //viewmode
                return executeQueryView(req.getParameter("view"));

            }
        } catch (Exception e) {
            return ExceptionUtil. getEx(e);
            //          res.getWriter().write(s);
//          res.getWriter().flush();
//          res.flushBuffer();
        }
        throw new RuntimeException("not imp");

    }


    private String executeQueryView(String view) {
        return view;
    }

    private String executeSp(String sp, HttpServletResponse res) throws Exception {

        //repalce cur usre info with cookie userinfo
        if (sp.contains("@当前用户信息@")) {

            String u_col = " '$uname','$usertype' ";
            //创建context
            VelocityContext context = new VelocityContext();
//添加数据
            String uname = CookieUtil.getValue(req, "curUserInfo.uname");
            context.put("uname", uname);
            context.put("usertype", CookieUtil.getValue(req, "curUserInfo.usertype"));

            u_col = velocityUtil.getTmpltCalcRzt(u_col, context);
            sp = sp.replaceAll("@当前用户信息@", u_col);

        }
        sp = "call " + sp;
        return executeSql(sp, res);

    }


    private String executeSql(String sql, HttpServletResponse res) throws IOException, OgnlException {
        SqlSessionFactory sqlSessionFactory = MybatisUtil.getSqlSessionFactory();
        SqlSession session = sqlSessionFactory.openSession(true);
//"call sp1(2)"
        MybatisMapperCls mapper = session.getMapper(MybatisMapperCls.class);

        List<Map> li = mapper.query(sql);
        String rzt = JSON.toJSONString(li, true);
        // System.out.println(rzt);
        session.close();
        return rzt;
//        res.getWriter().write(rzt);
//        res.getWriter().flush();
//        res.flushBuffer();
    }
}
