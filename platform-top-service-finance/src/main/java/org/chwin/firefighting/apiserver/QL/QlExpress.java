package org.chwin.firefighting.apiserver.QL;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
@Slf4j
public class QlExpress {
    @SneakyThrows
    public static void main(String[] args) {
        String s = "f1('abc',f2(123,456))";
        s="_@myfun(frm(tab),whereCondt('c=5'),andCondt('d=xxx'),selectCol(a,b,c))";
      //  s="myfun(fromx('tab'))";
        Function funAst = getFunctionAst(s);
        String x = toJsonString(funAst);
        System.out.println(
                x);

    }

    @NotNull
    public static String toJsonString(Function funAst) {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Function.class, "name", "parameters");

        SimplePropertyPreFilter filter2 = new SimplePropertyPreFilter(Column.class, "columnName");
        SimplePropertyPreFilter[] flts=new SimplePropertyPreFilter[]{filter,filter2};
        return JSON.toJSONString(funAst, flts, SerializerFeature.PrettyFormat);
    }

    //for druid ast
    public static String toJsonString(Object funAst) {
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Function.class, "name", "parameters");

        SimplePropertyPreFilter filter2 = new SimplePropertyPreFilter(Column.class, "columnName");
        SimplePropertyPreFilter[] flts=new SimplePropertyPreFilter[]{filter,filter2};
        return JSON.toJSONString(funAst, flts, SerializerFeature.PrettyFormat);
    }

//    public static String toJsonString(Function funAst) {
//        SimplePropertyPreFilter filter = new SimplePropertyPreFilter(Function.class, "name", "parameters");
//
//        SimplePropertyPreFilter filter2 = new SimplePropertyPreFilter(Column.class, "columnName");
//        SimplePropertyPreFilter[] flts=new SimplePropertyPreFilter[]{filter,filter2};
//        return JSON.toJSONString(funAst, flts, SerializerFeature.PrettyFormat);
//    }

    private static Function getFunctionAst(String s) throws JSQLParserException {
        String sql = "select " + s;
        log.info(sql);
        Statement Statement1 = CCJSqlParserUtil.parse(sql);
        Select select = (Select) Statement1;
        List<SelectItem> li = ((PlainSelect) select.getSelectBody()).getSelectItems();
        SelectExpressionItem sei = (SelectExpressionItem) li.get(0);
        return (Function) sei.getExpression();
    }
}
