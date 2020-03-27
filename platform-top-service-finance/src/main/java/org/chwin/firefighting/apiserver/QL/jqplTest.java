package org.chwin.firefighting.apiserver.QL;

import lombok.SneakyThrows;
import org.chwin.firefighting.apiserver.QL.JqplUtil;

public class jqplTest {
    private static final Object 页 = "";
    private static final Object 条数据 ="" ;
    private static int pagesize;
    private static int page;

    @SneakyThrows
    public static void main(String[] args) {


        //     System.out.println(JqplAst2SqlAstConverter.Jqpl2sql(hqlAst));   and c3=256
        // and c2=156   and c2=456  and c3=789 and c4=012
        String jqpl转换sql结果 = "aa";
        String jpql = "  from 表格1 where 字段1=123 ";
        Object 转换sql结果 = null;
        jpql = "select 字段2,字段3 from 表格1 where 字段1=123  and c2 =456   order by c1 desc  ";


        //   jpql="update t set a=1,b=2 where c=3  ";
        jpql = "update t set a=1,b=2 where c=3 and d=4";


        // System.out.println(JqplUtil.Jqpl2sql(jpql,"limit 33,10"));






        jpql = "select 字段2,字段3 from 表格1 where 字段1=123  and c2 =456   order by c1 desc  ";
        jpql = "select a,b from 表格1 where c=123  and d =456   order by c1 desc ,c2 ,c3 desc";
    //    jpql = "select a,b from 表格1";
    //    jpql = "update t set a=1,b=2 where c=3 and d=4";
   //     jpql = "  from 表格1 where c=123  and d =456   order by c1 desc  ";
   //     jpql="delete  from t where c=1 and d=2";
        JqplUtil.showAst(jpql);
        System.out.println(JqplUtil.Jqpl2sql(jpql,"limit 33,10"));
      //输出( $(jpql,转换sql结果), 翻页设置是第(3).页, 每页显示(30).条数据);
























    }

    private static Object $(String jpql, Object 转换sql结果) {
        return jpql;
    }

    private static String $(String jpql) {
        return jpql;
    }

    private static void 输出(Object jpql,  Object 页, Object 条数据) {
        System.out.println(JqplUtil.Jqpl2sql(jpql.toString(), String.format("limit %d,%d", jqplTest.pagesize * (jqplTest.page - 1), jqplTest.pagesize)));
    }

    private static void 输出(String jpql, Object jqpl转换sql结果, Object 页, Object 条数据) {
        System.out.println(JqplUtil.Jqpl2sql(jpql, String.format("limit %d,%d", jqplTest.pagesize * (jqplTest.page - 1), jqplTest.pagesize)));
    }

    private static jqplTest 每页显示(int pagesize) {
        jqplTest.pagesize = pagesize;
        return new jqplTest();
    }

    private static jqplTest 翻页设置是第(int page) {
        jqplTest.page = page;
        return new jqplTest();
    }
}
